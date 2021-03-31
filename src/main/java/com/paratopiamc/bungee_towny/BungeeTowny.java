package com.paratopiamc.bungee_towny;

import com.paratopiamc.bungee_towny.bungeemessage.BungeeMessage;
import com.paratopiamc.bungee_towny.bungeemessage.BungeeMessageListener;
import com.paratopiamc.bungee_towny.chat.channel.Channels;
import com.paratopiamc.bungee_towny.command.AdminCommandExecutor;
import com.paratopiamc.bungee_towny.command.AdminCommandTabCompletor;
import com.paratopiamc.bungee_towny.command.chat.IgnoreCommandExecutor;
import com.paratopiamc.bungee_towny.command.chat.msg.MsgCommandTabCompletor;
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

    private static String serverUUID;
    private static String serverName;

    public static BukkitTask waitForBungeePlayer;
    private static BukkitTask getPlayerList;

    private static JavaPlugin thisPlugin;

    private static boolean isSpigot = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        thisPlugin = this;

        //Enable listeners
        Listeners.init(this);

        reload();

        try { //We use a try catch to avoid errors, hopefully we don't get any.
            Class.forName("com.mysql.jdbc.Driver"); //this accesses Driver in jdbc.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            getLogger().log(Level.SEVERE, "jdbc sql driver unavailable!");

            Bukkit.getPluginManager().disablePlugin(this);
        }

        //Check for event hooks
        //Towny
        if (Bukkit.getPluginManager().getPlugin("Towny") == null) {
            getLogger().info("Could not find Towny. Your server may be missing some functionality");
        } else {
            Listeners.usingTowny(true);
        }

        waitForBungeePlayer = new BukkitRunnable() {
            @Override
            public void run() {
                if (!isCancelled()) {
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
            }
        }.runTaskTimer(this, 20, 60 * 20); //60 second timeframe

        getPlayerList = new BukkitRunnable() {
            @Override
            public void run() {
                bungeeMessager.writeStrings(new String[]{"PlayerList", "ALL"});
                bungeeMessager.send();
            }
        }.runTaskTimer(this, 20, 20 * 15); //15 s timeframe

        //bstats integration
        int pluginId = 10724;
        Metrics metrics = new Metrics(this, pluginId);

        getCommand("bungeetowny").setExecutor(new AdminCommandExecutor());
        getCommand("bungeetowny").setTabCompleter(new AdminCommandTabCompletor());

        try {
            if (Class.forName("org.spigotmc.SpigotConfig") != null) {
                // has spigot
                isSpigot = true;
                getLogger().info("This server runs spigot!");
            }
        } catch (ClassNotFoundException e) {}

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

        getLogger().info("Cancelling tasks..");
        try {
            waitForBungeePlayer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            getPlayerList.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }

        getLogger().info("BungeeTowny Disabled");
    }

    public static void setServerName(String name) {
        if (name == null) {
            return;
        }

        File server_uuid_yml = new File(getThisPlugin().getDataFolder(), "server_uuid.yml");

        if (serverName != name) {

            getThisPlugin().getLogger().info("The server's name has been changed to " + name);

            FileConfiguration server_uuid_config = YamlConfiguration.loadConfiguration(server_uuid_yml);

            server_uuid_config.set("this_server.name", name);

            serverName = name;

            update_server_listing();

            try {
                //TODO file header (for comment)
                server_uuid_config.save(server_uuid_yml);
            } catch (IOException exception) {
                exception.printStackTrace();

                //TODO this
                BungeeTowny.getThisPlugin().getLogger().log(Level.SEVERE, "The UUID file cannot be saved! Check your file permissions");
            }
        }
    }

    public static String getServerName() {
        return serverName;
    }

    public static String getServerUUID() {
        return serverUUID;
    }

    static void update_server_listing() {
        SQLHost.set_server(serverUUID, serverName);
    }

    public static void reload() {

        //vanilla config file
        thisPlugin.saveDefaultConfig();

        //set the config lang
        String lang = thisPlugin.getConfig().getString("lang");
        if (lang != null) {
            Translation.setLang(lang);
        }
        thisPlugin.getLogger().info("Using language " + lang);

        reloadSQL();

        reloadServer();

        reloadChat();

        //TODO sync configs

        Listeners.reload();
    }

    public static void reloadMessages() {
        Translation.reset();

        //chat/<file>
        if (thisPlugin.getConfig().getBoolean("chat")) {
            //messages ==================================================================================
            File messageFile = new File(thisPlugin.getDataFolder(), "chat/Messages.yml");
            if (!messageFile.exists()) {
                thisPlugin.saveResource("chat/Messages.yml", false);
            }

            messageFile = new File(thisPlugin.getDataFolder(), "chat/Messages.yml");
            FileConfiguration messageConfig = YamlConfiguration.loadConfiguration(messageFile);

            Translation.setFromConfig(messageConfig, "chat");

            SQLHost.set_config("chatMessages", messageFile);
        }

        //global
        //Messages ===================================================================
        File messageFile = new File(thisPlugin.getDataFolder(), "Messages.yml");
        if (!messageFile.exists()) {
            thisPlugin.saveResource("Messages.yml", false);
        }

        messageFile = new File(thisPlugin.getDataFolder(), "Messages.yml");
        FileConfiguration messageConfig = YamlConfiguration.loadConfiguration(messageFile);

        Translation.setFromConfig(messageConfig, "towny");

    }

    public static void reloadSQL() {
        //SQL
        File sqlfile = new File(thisPlugin.getDataFolder(), "mysql.yml");
        if (!sqlfile.exists()) {
            thisPlugin.saveResource("mysql.yml", false);
        }
        FileConfiguration sqlconfig = YamlConfiguration.loadConfiguration(sqlfile);

        SQLHost.init(sqlconfig.getConfigurationSection(""));
    }

    public static void reloadServer() {
        //server UUID file
        File server_uuid_yml = new File(thisPlugin.getDataFolder(), "server_uuid.yml");
        FileConfiguration server_uuid_config = YamlConfiguration.loadConfiguration(server_uuid_yml);

        boolean loaded_uuid = server_uuid_yml.exists();

        if (!loaded_uuid) {
            thisPlugin.saveResource("server_uuid.yml", false);

            serverUUID = UUID.randomUUID().toString();

            server_uuid_config.set("this_server.uuid", serverUUID);

        } else {
            serverUUID = server_uuid_config.getString("this_server.uuid");
            serverName = server_uuid_config.getString("this_server.name");
        }

        thisPlugin.getLogger().info("This server's UUID is " + serverUUID + " " + (loaded_uuid ? "(from file)" : "(newly generated)"));
        thisPlugin.getLogger().info("This server's name is " + serverName);
        update_server_listing();

        try {
            //TODO file header (for comment)
            server_uuid_config.save(server_uuid_yml);
        } catch (IOException exception) {
            exception.printStackTrace();

            //TODO this
            BungeeTowny.getThisPlugin().getLogger().log(Level.SEVERE, "The UUID file cannot be saved! Check your file permissions");
        }
    }

    public static void reloadChat() {
        thisPlugin.reloadConfig();
        reloadMessages();

        //figure out if chat features are enabled, and do the thing!
        //chat/<file>
        if (thisPlugin.getConfig().getBoolean("chat")) {
            Listeners.usingChat(true);

            //PlaceholderAPI
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
                thisPlugin.getLogger().info("Could not find PlaceholderAPI. Your chat may be missing some functionality");
            } else {
                Listeners.usingPAPI(true);
            }

            //readme ===================================================================================
            thisPlugin.saveResource("chat/README.txt", true);

            //channels ==================================================================================
            File channelFile = new File(thisPlugin.getDataFolder(), "chat/Channels.yml");
            if (!channelFile.exists()) {
                thisPlugin.saveResource("chat/Channels.yml", false);
            }

            channelFile = new File(thisPlugin.getDataFolder(), "chat/Channels.yml");
            FileConfiguration channelConfig = YamlConfiguration.loadConfiguration(channelFile);

            SQLHost.set_config("channels", channelFile);

            //formats ==================================================================================
            File chatConfigFile = new File(thisPlugin.getDataFolder(), "chat/ChatConfig.yml");
            if (!chatConfigFile.exists()) {
                thisPlugin.saveResource("chat/ChatConfig.yml", false);
            }

            chatConfigFile = new File(thisPlugin.getDataFolder(), "chat/ChatConfig.yml");
            FileConfiguration chatConfig = YamlConfiguration.loadConfiguration(chatConfigFile);

            Translation.addKeys(chatConfig.getConfigurationSection("colour"), "chat.towny.colors");
            Translation.addKeys(chatConfig.getConfigurationSection("tag_formats"), "chat.towny.formats");
            
            SQLHost.set_config("chatConfig", chatConfigFile);

            //BungeeTowny ================================================================================
            File newChatSettingsFile = new File(thisPlugin.getDataFolder(), "chat/BungeeTowny.yml");
            if (!newChatSettingsFile.exists()) {
                thisPlugin.saveResource("chat/BungeeTowny.yml", false);
            }

            newChatSettingsFile = new File(thisPlugin.getDataFolder(), "chat/BungeeTowny.yml");
            FileConfiguration newChatConfig = YamlConfiguration.loadConfiguration(newChatSettingsFile);

            SQLHost.set_config("newChatSettings", newChatSettingsFile);

            //get the channels as commands
            ConfigurationSection channels = channelConfig.getConfigurationSection("Channels");

            Channels.init(thisPlugin, channels, chatConfig, newChatConfig);

            thisPlugin.getCommand("ignore").setExecutor(new IgnoreCommandExecutor());
            thisPlugin.getCommand("ignore").setTabCompleter(new MsgCommandTabCompletor());

        } else {
            Listeners.usingChat(false);

            Channels.unRegisterChannels();
        }
    }

    public static Plugin getThisPlugin() {
        return thisPlugin;
    }

    /*public static JavaPlugin getJavaPlugin() {
        return thisPlugin;
    }*/
}
