package com.paratopiamc.bungee_towny.chat.chatcommand;

import com.paratopiamc.bungee_towny.Translation;
import com.paratopiamc.bungee_towny.chat.Channel.Channel;
import com.paratopiamc.bungee_towny.chat.ChatSendEvent;
import com.paratopiamc.bungee_towny.synced.Players;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ChatCommandExecutor implements CommandExecutor {

    private static HashMap<String, Channel> channels = new HashMap<>();

    private static String no_town;
    private static String no_nation;

    public ChatCommandExecutor(HashMap<String, Channel> channels) {
        no_town = replaceColors(Translation.of("chat.no_town"));
        no_nation = replaceColors(Translation.of("chat.no_nation"));

        this.channels = channels;
    }

    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = ((Player) sender).getPlayer();
        String uuid = player.getUniqueId().toString();

        if (args.length < 1) {
            // /chat
            sender.sendMessage(replaceColors(Translation.of("chat.current_channel").replace("${CHANNEL}", Players.getChannel(uuid))));
            return true;
        } else if (args.length == 1) {
            // /chat <channel>
            for (Channel channel : channels.values()) {
                if (args[0].equalsIgnoreCase(channel.getName()) && player.hasPermission(channel.getPermission())) {
                    if (channel.getName().equals("town") && Players.getTown(uuid).equalsIgnoreCase("townless")) {
                        sender.sendMessage(no_town);

                        //TODO not this
                        Bukkit.dispatchCommand(player, "chat general");
                        return true;
                    }
                    if (channel.getName().equals("nation") && Players.getNation(uuid).equalsIgnoreCase("nationless")) {
                        sender.sendMessage(no_nation);

                        //TODO not this
                        Bukkit.dispatchCommand(player, "chat general");
                        return true;
                    }

                    sender.sendMessage(replaceColors(Translation.of("chat.channel_switch").replace("${CHANNEL}", channel.getName())));


                    //switch the channel
                    Players.setChannel(channel.getName(), uuid);

                    return true;
                }
            }

            sender.sendMessage(replaceColors(Translation.of("chat.not_a_channel")));
            return true;
        } else {
            // /chat <channel> <msg>
            String message = "";
            for (short i = 1; i < args.length; i++) {
                message += args[i];
            }

            for (Channel channel : channels.values()) {
                if (args[0].equalsIgnoreCase(channel.getName()) && player.hasPermission(channel.getPermission())) {
                    if (channel.getName().equals("town") && Players.getTown(uuid).equalsIgnoreCase("townless")) {
                        sender.sendMessage(no_town);

                        //TODO not this
                        Bukkit.dispatchCommand(player, "chat general");
                        return true;
                    }
                    if (channel.getName().equals("nation") && Players.getNation(uuid).equalsIgnoreCase("nationless")) {
                        sender.sendMessage(no_nation);

                        //TODO not this
                        Bukkit.dispatchCommand(player, "chat general");
                        return true;
                    }

                    ChatSendEvent event = new ChatSendEvent(message, channel, ((Player) sender).getPlayer(), !Bukkit.isPrimaryThread());
                    Bukkit.getPluginManager().callEvent(event);

                    return true;
                }
            }
        }

        return false;
    }

    private String replaceColors(String message) {
        return message.replace("&", "\u00a7");
    }
}