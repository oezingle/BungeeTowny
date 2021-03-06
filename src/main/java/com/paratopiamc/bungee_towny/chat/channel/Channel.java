package com.paratopiamc.bungee_towny.chat.channel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.paratopiamc.bungee_towny.command.chat.channel.ChatAliasExecutor;
import com.paratopiamc.bungee_towny.command.chat.msg.MsgCommandAlias;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Channel {
    private ChannelType type;

    private String permission;
    private String name;
    private String format;
    private List<String> commands;
    private List<BukkitCommand> executors;

    private int range;

    private boolean isJSON;

    public Channel(ConfigurationSection config, String format) {

        permission = config.getString("permission");
        type = ChannelType.fromString(config.getString("type"));
        if (type != ChannelType.MESSAGE) {
            name = config.getName();
        } else {
            name = "msg";
        }

        commands = config.getStringList("commands");

        range = config.getInt("range");

        executors = new ArrayList<>();

        for (String command : commands) {
            registerAliasCommand(command);
        }

        String channeltag = config.getString("channeltag");
        String messagecolour = config.getString("messagecolour");

        this.format = format
                .replace("{channelTag}",channeltag)
                .replace("{msgcolour}", messagecolour);

        try {
            JsonObject jsonObject = new Gson().fromJson(format, JsonObject.class);

            isJSON = jsonObject.has("text");
        } catch (JsonSyntaxException e) {
            isJSON = false;
        }

        //BungeeTowny.getThisPlugin().getLogger().info("Registered chat channel: " + name);
    }

    public String getFormat() {
        return format;
    }

    public ChannelType getType() {
        return type;
    }

    public String getPermission() {
        return permission;
    }

    public String getName() {
        return name;
    }

    public boolean isJSON() {
        return isJSON;
    }

    boolean registerAliasCommand(String command) {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            BukkitCommand executor;
            if (name == "msg") {
                executor = new MsgCommandAlias(command);
            } else {
                executor = new ChatAliasExecutor(command, name);
            }
            commandMap.register("bungeetowny", executor);

            executors.add(executor);

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    /*void registerAlias(String command) {
        List<String> aliases = executor.getAliases();
        aliases.add(command);

        executor.setAliases(aliases);

    }*/

    public void unRegister() {
        try {
            for (BukkitCommand executor : executors) {
                final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                bukkitCommandMap.setAccessible(true);

                CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

                executor.unregister(commandMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //BungeeTowny.getThisPlugin().getLogger().info("[BungeeTowny] Unregistered chat channel: " + name);
    }
}
