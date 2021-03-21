package com.paratopiamc.bungee_towny.command.notowny;

import com.paratopiamc.bungee_towny.Translation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TownCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
        if (args.length == 0) {
            //TODO town status
            sender.sendMessage(Translation.of("towny.command.not_found"));
            return true;
        } else if (args.length == 1 ) {
            switch(args[0]) {
                case "spawn":
                    if (sender.hasPermission("bungeetowny.town.spawn")) {


                    } else {
                        sender.sendMessage(Translation.of("towny.command.not_found"));
                    }

            }
        } else {
            sender.sendMessage(Translation.of("towny.command.not_found"));
        }

        return false;
    }
}