package com.paratopiamc.bungee_towny.chat;

import com.paratopiamc.bungee_towny.chat.channel.Channel;
import com.paratopiamc.bungee_towny.chat.chatcommand.ChatCommandExecutor;
import com.paratopiamc.bungee_towny.chat.chatcommand.ChatCommandTabCompletor;
import com.paratopiamc.bungee_towny.chat.mutecommand.MuteCommandExecutor;
import com.paratopiamc.bungee_towny.chat.mutecommand.UnmuteCommandExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public abstract class Channels {

    private static HashMap<String, Channel> channels = new HashMap<>();

    public static void init(JavaPlugin plugin, ConfigurationSection channelConfig, ConfigurationSection chatConfig) {
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
                System.out.println("Channel " + key + " could not be registered");
            }
        }

        plugin.getCommand("chat").setExecutor(new ChatCommandExecutor(channels));
        plugin.getCommand("chat").setTabCompleter(new ChatCommandTabCompletor(channels));

        plugin.getCommand("mute").setExecutor(new MuteCommandExecutor());
        plugin.getCommand("unmute").setExecutor(new UnmuteCommandExecutor());

    }

    public static void unRegisterChannels() {
        for (Channel channel : channels.values()) {
            //channel.unRegister();
        }
    }

    public static Channel get(String channel) {
        return channels.get(channel);
    }
}
