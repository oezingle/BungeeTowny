package com.paratopiamc.bungee_towny;

import com.paratopiamc.bungee_towny.bungeemessage.BungeeMessage;
import com.paratopiamc.bungee_towny.bungeemessage.BungeeMessageListener;
import com.paratopiamc.bungee_towny.chat.Channels;
import com.paratopiamc.bungee_towny.chat.ChatColors;
import com.paratopiamc.bungee_towny.chat.ChatFormats;
import com.paratopiamc.bungee_towny.listener.Listeners;
import com.paratopiamc.bungee_towny.sql.SQLHost;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

import org.bstats.bukkit.Metrics;

public final class BungeeTowny extends JavaPlugin {

    static BungeeMessage bungeeMessager;
    static BungeeMessageListener bungeeListener;

    static String serverUUID;
    static String serverName;

    static File server_uuid_yml;

    public static BukkitTask waitForBungeePlayer;

    @Override
    public void onEnable() {
        // Plugin startup logic

        //vanilla config file
        saveDefaultConfig();

        //set the config lang
        String lang = getConfig().getString("lang");
        if (lang != null) {
            Translation.setLang(lang);
        }
        getLogger().info("Using language " + lang);

        //server UUID file
        server_uuid_yml = new File(getDataFolder(), "server_uuid.yml");
        FileConfiguration server_uuid_config = YamlConfiguration.loadConfiguration(server_uuid_yml);

        boolean loaded_uuid = server_uuid_yml.exists();

        if (!loaded_uuid) {
            saveResource("server_uuid.yml", false);

            serverUUID = UUID.randomUUID().toString();

            server_uuid_config.set("this_server.uuid", serverUUID);

            save_uuid_config(server_uuid_config);
        } else {
            serverUUID = server_uuid_config.getString("this_server.uuid");
            serverName = server_uuid_config.getString("this_server.name");
        }

        //SQL
        File sqlfile = new File(getDataFolder(), "mysql.yml");
        if (!sqlfile.exists()) {
            saveResource("mysql.yml", false);
        }
        FileConfiguration sqlconfig = YamlConfiguration.loadConfiguration(sqlfile);

        SQLHost.init(sqlconfig.getConfigurationSection(""));

        try { //We use a try catch to avoid errors, hopefully we don't get any.
            Class.forName("com.mysql.jdbc.Driver"); //this accesses Driver in jdbc.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            getLogger().log(Level.SEVERE, "jdbc sql driver unavailable!");

            Bukkit.getPluginManager().disablePlugin(this);
        }

        //loading is done
        getLogger().info("This server's UUID is " + serverUUID + " " + (loaded_uuid ? "(from file" : "(newly generated)"));
        getLogger().info("This server's name is " + serverName);
        update_server_listing();

        //Check for event hooks
        //Towny
        if (Bukkit.getPluginManager().getPlugin("Towny") == null) {
            getLogger().info("Could not find Towny. Your server may be missing some functionality");


        } else {
            Listeners.usingTowny(true);
        }

        //figure out if chat features are enabled, and do the thing!
        if (getConfig().getBoolean("chat")) {
            Listeners.usingChat(true);

            //PlaceholderAPI
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
                getLogger().info("Could not find PlaceholderAPI. Your chat may be missing some functionality");
            } else {
                Listeners.usingPAPI(true);
            }

            //readme ===================================================================================
            saveResource("chat/README.txt", true);

            //channels ==================================================================================
            File channelFile = new File(getDataFolder(), "chat/Channels.yml");
            if (!channelFile.exists()) {
                saveResource("chat/Channels.yml", false);
            }

            channelFile = new File(getDataFolder(), "chat/Channels.yml");
            FileConfiguration channelConfig = YamlConfiguration.loadConfiguration(channelFile);

            SQLHost.set_config("channels", channelFile);

            //formats ==================================================================================
            File chatConfigFile = new File(getDataFolder(), "chat/ChatConfig.yml");
            if (!chatConfigFile.exists()) {
                saveResource("chat/ChatConfig.yml", false);
            }

            chatConfigFile = new File(getDataFolder(), "chat/ChatConfig.yml");
            FileConfiguration chatConfig = YamlConfiguration.loadConfiguration(chatConfigFile);

            ChatColors.init(chatConfig.getConfigurationSection("colour"));
            ChatFormats.init(chatConfig.getConfigurationSection("tag_formats"));

            SQLHost.set_config("chatConfig", chatConfigFile);

            //messages ==================================================================================
            File messageFile = new File(getDataFolder(), "chat/Messages.yml");
            if (!messageFile.exists()) {
                saveResource("chat/Messages.yml", false);
            }

            messageFile = new File(getDataFolder(), "chat/Messages.yml");
            FileConfiguration messageConfig = YamlConfiguration.loadConfiguration(messageFile);

            Translation.setFromConfig(messageConfig, "chat.");

            SQLHost.set_config("chatMessages", messageFile);

            //BungeeTowny ================================================================================
            File newChatSettingsFile = new File(getDataFolder(), "chat/BungeeTowny.yml");
            if (!newChatSettingsFile.exists()) {
                saveResource("chat/BungeeTowny.yml", false);
            }

            newChatSettingsFile = new File(getDataFolder(), "chat/BungeeTowny.yml");
            FileConfiguration newChatConfig = YamlConfiguration.loadConfiguration(chatConfigFile);

            SQLHost.set_config("newChatSettings", newChatSettingsFile);

            //get the channels as commands
            ConfigurationSection channels = channelConfig.getConfigurationSection("Channels");

            Channels.init(this, channels, chatConfig);
        }

        //Messages ===================================================================
        File messageFile = new File(getDataFolder(), "Messages.yml");
        if (!messageFile.exists()) {
            saveResource("Messages.yml", false);
        }

        messageFile = new File(getDataFolder(), "Messages.yml");
        FileConfiguration messageConfig = YamlConfiguration.loadConfiguration(messageFile);

        Translation.setFromConfig(messageConfig, "towny");

        //TODO sync configs

        Plugin thisPlugin = this;
        waitForBungeePlayer = new BukkitRunnable() {
            @Override
            public void run() {
                //TODO repeat until a player can send the bungeemessage
                //load in bungeeMessage
                //Tx
                getServer().getMessenger().registerOutgoingPluginChannel(thisPlugin, "BungeeCord");
                //Rx
                bungeeListener = new BungeeMessageListener(thisPlugin);
                getServer().getMessenger().registerIncomingPluginChannel(thisPlugin, "BungeeCord", bungeeListener);

                bungeeMessager = new BungeeMessage(thisPlugin);

                bungeeMessager.writeString("GetServer");
                bungeeMessager.send();
            }
        }.runTaskTimer(this, 20, 40); //2 second timeframe

        //Enable listeners
        Listeners.init(this);

        //Bstats lmao
        int pluginId = 10724;
        Metrics metrics = new Metrics(this, pluginId);

        getLogger().info("BungeeTown Enabled!");
    }

    @Override
    public void onDisable() {
        //remove listeners
        getLogger().info("Unloading Listeners..");
        Listeners.unload();

        //remove commands
        getLogger().info("Removing commands..");
        Channels.unRegisterChannels();
        getCommand("chat").setExecutor(null);

        getLogger().info("BungeeTowny Disabled");
    }

    public static void setServerName(String name) {
        if (!serverName.equalsIgnoreCase(name)) {
            System.out.println("[BungeeTown] The server's name has been changed to " + name);

            FileConfiguration server_uuid_config = YamlConfiguration.loadConfiguration(server_uuid_yml);

            server_uuid_config.set("this_server.name", name);

            save_uuid_config(server_uuid_config);

            update_server_listing();
        }
        serverName = name;
    }

    public static String getServerName() {
        return serverName;
    }

    public static String getServerUUID() {
        return serverUUID;
    }

    static void save_uuid_config(FileConfiguration server_uuid_config) {
        try {
            //TODO file header (for comment)
            server_uuid_config.save(server_uuid_yml);
        } catch (IOException exception) {
            exception.printStackTrace();

            //TODO this
            //getLogger().log(Level.SEVERE, "The UUID file cannot be saved! Check your file permissions");
            System.err.println("[BungeeTown] The UUID file cannot be saved! Check your file permissions");
        }
    }

    //logger for other classes
    public void mainLogger(String msg) {
        getLogger().info(msg);
    }

    static void update_server_listing() {
        //not required because of the other servers
        //Servers.set_uuid(serverUUID, serverName);
        SQLHost.set_server(serverUUID, serverName, Listeners.isUsingTowny());

    }

    static void reload() {
        Listeners.reload();
    }
}
