package com.paratopiamc.bungee_towny.command.chat.channel;

import com.paratopiamc.bungee_towny.Translation;
import com.paratopiamc.bungee_towny.chat.channel.Channel;
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
        no_town = Translation.of("chat.no_town");
        no_nation = Translation.of("chat.no_nation");

        this.channels = channels;
    }

    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(replaceColors("&cThis sender isn't allowed to send this command"));
            return true;
        }

        Player player = ((Player) sender).getPlayer();
        String uuid = player.getUniqueId().toString();

        Players players = new Players();

        if (args.length < 1) {
            // /chat
            sender.sendMessage(Translation.of("chat.current_channel").replace("{channel}", players.getChannel(uuid)));
            return true;
        } else {
            for (Channel channel : channels.values()) {
                if (channel.getName().equals("msg")) {
                    continue;
                }

                if (args[0].equalsIgnoreCase(channel.getName()) && sender.hasPermission(channel.getPermission())) {
                    if (channel.getName().equals("town") && players.getTown(uuid).equalsIgnoreCase("townless")) {
                        sender.sendMessage(no_town);
                        return true;
                    }
                    if (channel.getName().equals("nation") && players.getNation(uuid).equalsIgnoreCase("nationless")) {
                        sender.sendMessage(no_nation);
                        return true;
                    }
                    if (channel.getName().equals("alliance")) {
                        sender.sendMessage(replaceColors("&cAlliance chats are currently not implemented"));
                        return true;
                    }

                    if (args.length == 1) {
                        sender.sendMessage(Translation.of("chat.channel_switch").replace("{channel}", channel.getName()));

                        //switch the channel
                        players.setChannel(channel.getName(), uuid);
                    } else {
                        // /chat <channel> <msg>
                        String message = "";
                        for (short i = 1; i < args.length; i++) {
                            message += args[i] + " ";
                        }
                        ChatSendEvent event = new ChatSendEvent(message, channel, ((Player) sender).getPlayer(), !Bukkit.isPrimaryThread());
                        Bukkit.getPluginManager().callEvent(event);

                    }
                    return true;
                }
            }

            //notify the user that the command doesn't exist
            sender.sendMessage(Translation.of("chat.not_a_channel"));
            return true;
        }
    }

    private String replaceColors(String message) {
        return message.replace("&", "\u00a7");
    }
}