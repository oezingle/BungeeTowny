package com.paratopiamc.bungee_towny.chat.channel;

import com.paratopiamc.bungee_towny.BungeeTowny;

import com.paratopiamc.bungee_towny.command.chat.channel.ChatCommandExecutor;
import com.paratopiamc.bungee_towny.command.chat.channel.ChatCommandTabCompletor;
import com.paratopiamc.bungee_towny.command.chat.msg.MsgCommandExecutor;
import com.paratopiamc.bungee_towny.command.chat.msg.MsgCommandTabCompletor;
import com.paratopiamc.bungee_towny.command.chat.mute.MuteCommandExecutor;
import com.paratopiamc.bungee_towny.command.chat.mute.UnmuteCommandExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Level;

public abstract class Channels {

    private static HashMap<String, Channel> channels = new HashMap<>();

    public static void init(JavaPlugin plugin, ConfigurationSection channelConfig, ConfigurationSection chatConfig, ConfigurationSection bungeeTownyChatConfig) {
        Set<String> keys = channelConfig.getKeys(false);

        ConfigurationSection chat_formats = chatConfig.getConfigurationSection("channel_formats");

        for (String key : keys) {
            ConfigurationSection channel = channelConfig.getConfigurationSection(key);
            String format = chat_formats.getString(channel.getString("type").toLowerCase());

            if (format == null ) {
                format = chat_formats.getString("default");
            }

            if (channel != null) {
                channels.put(key, new Channel(channel, format));
            } else {
                BungeeTowny.getThisPlugin().getLogger().log(Level.WARNING, "Channel " + key + " could not be registered");
            }
        }

        plugin.getCommand("chat").setExecutor(new ChatCommandExecutor(channels));
        plugin.getCommand("chat").setTabCompleter(new ChatCommandTabCompletor(channels));

        plugin.getCommand("mute").setExecutor(new MuteCommandExecutor());
        plugin.getCommand("unmute").setExecutor(new UnmuteCommandExecutor());

        ConfigurationSection msgConfig = bungeeTownyChatConfig.getConfigurationSection("msg");
        if (msgConfig.getBoolean("enabled")) {
            ConfigurationSection channel = msgConfig.getConfigurationSection("channel");
            String format = msgConfig.getString("format");

            if (format == null ) {
                format = chat_formats.getString("default");
            }

            if (channel != null) {
                channels.put("msg", new Channel(channel, format));
            } else {
                BungeeTowny.getThisPlugin().getLogger().log(Level.WARNING, "Channel msg could not be registered");
            }
            plugin.getCommand("message").setExecutor(new MsgCommandExecutor(msgConfig));
            plugin.getCommand("message").setTabCompleter(new MsgCommandTabCompletor());
        }
    }

    public static void unRegisterChannels() {
        for (Channel channel : channels.values()) {
            channel.unRegister();
        }
    }

    public static Channel get(String channel) {
        return channels.get(channel);
    }
}
