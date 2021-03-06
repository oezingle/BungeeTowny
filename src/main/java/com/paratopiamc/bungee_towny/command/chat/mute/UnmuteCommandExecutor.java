package com.paratopiamc.bungee_towny.command.chat.mute;

import com.paratopiamc.bungee_towny.Translation;
import com.paratopiamc.bungee_towny.synced.Players;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnmuteCommandExecutor implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {

        if (args.length < 1 || args.length > 1) {
            sender.sendMessage(Translation.of("chat.bad_args"));
            return false;
        }

        //TODO how do I do this without a depreciated function
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(args[0])) {
                //mute the player
                String uuid = player.getUniqueId().toString();
                String name = player.getName();

                if (! new Players().isMuted(uuid)) {
                    sender.sendMessage(Translation.of("chat.admin.already_unmuted"));
                    return true;
                } else {
                    sender.sendMessage(Translation.of("chat.admin.unmute_success").replace("{playername}",name));
                    new Players().setMuted(uuid, false);
                }

                //notify them
                //TODO check for chat/BungeeTowny.yml > mute.notify
                player.sendMessage(Translation.of("chat.unmuted"));

                //return true
                return true;
            }
        }

        sender.sendMessage(Translation.of("chat.admin.player_not_found"));
        return true;
    }
}