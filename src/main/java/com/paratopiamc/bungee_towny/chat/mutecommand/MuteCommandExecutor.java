package com.paratopiamc.bungee_towny.chat.mutecommand;

import com.paratopiamc.bungee_towny.Translation;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommandExecutor implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {

        if (args.length < 1 || args.length > 1) {
            sender.sendMessage(replaceColors(Translation.of("chat.bad_args")));
            return false;
        }

        //TODO how do I do this without a depreciated function
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(args[0])) {
                //mute the player

                //notify them
                player.sendMessage(Translation);

                //return true
                return true;
            }
        }

        sender.sendMessage(replaceColors(Translation.of("chat.admin.player_not_found")));
    }

    private String replaceColors(String message) {
        return message.replace("&", "\u00a7");
    }

}