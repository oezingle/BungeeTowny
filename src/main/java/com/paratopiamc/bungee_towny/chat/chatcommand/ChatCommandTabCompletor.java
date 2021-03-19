package com.paratopiamc.bungee_towny.chat.chatcommand;

import com.paratopiamc.bungee_towny.chat.Channel.Channel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatCommandTabCompletor implements TabCompleter {

    private static HashMap<String, Channel> channels = new HashMap<>();

    public ChatCommandTabCompletor(HashMap<String, Channel> channels) {
        this.channels = channels;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> results = new ArrayList<>();
        if (args.length == 1) {

            for (Channel channel : channels.values()) {
                if (sender.hasPermission(channel.getPermission())) {
                    results.add(channel.getName());
                }
            }
        }
        return results;
    }
}
