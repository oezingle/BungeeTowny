package com.paratopiamc.bungee_towny.chat.channel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.bukkit.configuration.ConfigurationSection;

public class Channel {
    private ChannelType type;
    //private ChannelCommandExecutor executor;

    private String permission;
    private String name;
    private String format;
    //private List<String> commands;

    private int range;

    private boolean isJSON;

    public Channel(ConfigurationSection config, String format) {

        permission = config.getString("permission");
        type = ChannelType.fromString(config.getString("type"));
        name = config.getName();

        //commands = config.getStringList("commands");

        range = config.getInt("range");

        //TODO add /chat <channel> and alias to that

        /*registerChannelCommand(commands.get(0));
        commands.remove(0);
        for (String command : commands) {
            registerAlias(command);
        }*/

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

        //System.out.println("Registered channel: " + name);
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

    /*boolean registerChannelCommand(String command) {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            executor = new ChannelCommandExecutor(command, name, permission);

            commandMap.register(command, executor);

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    void registerAlias(String command) {
        List<String> aliases = executor.getAliases();
        aliases.add(command);

        executor.setAliases(aliases);

    }*/

    /*public void unRegister() {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);

            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            executor.unregister(commandMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Unregistered channel: " + name);
    }*/
}