package com.paratopiamc.bungee_towny.command;

import com.paratopiamc.bungee_towny.BungeeTowny;
import com.paratopiamc.bungee_towny.Translation;
import com.paratopiamc.bungee_towny.listener.Listeners;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AdminCommandExecutor implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Translation.of("towny.command.admin.help.row1"));
            sender.sendMessage(Translation.of("towny.command.admin.help.row2"));
            sender.sendMessage(Translation.of("towny.command.admin.help.row3"));
            sender.sendMessage(Translation.of("towny.command.admin.help.row4"));

            return true;
        } else {
            switch(args[0]) {
                case "reload":
                    if (args.length == 1) {
                        BungeeTowny.reload();
                        sender.sendMessage(Translation.of("towny.command.admin.reload_success").replace("{module}",""));
                        return true;
                    } else if (args.length == 2) {
                        //reload <module>
                        switch(args[1]) {
                            case "chat":
                                BungeeTowny.reloadChat();
                                sender.sendMessage(Translation.of("towny.command.admin.reload_success").replace("{module}","chat"));
                                return true;
                            case "sql":
                                BungeeTowny.reloadSQL();
                                sender.sendMessage(Translation.of("towny.command.admin.reload_success").replace("{module}","sql"));
                                return true;
                            case "messages":
                                BungeeTowny.reloadMessages();
                                sender.sendMessage(Translation.of("towny.command.admin.reload_success").replace("{module}","messages"));
                                return true;
                            case "server":
                                BungeeTowny.reloadServer();
                                sender.sendMessage(Translation.of("towny.command.admin.reload_success").replace("{module}","server"));
                                return true;
                            default:
                                sender.sendMessage(Translation.of("towny.command.admin.unknown_arg"));
                                return false;
                        }
                    } else {
                        sender.sendMessage(Translation.of("towny.command.admin.unknown_arg"));
                        break;
                    }
                case "status":
                    if (Listeners.isUsingTowny()) {
                        sender.sendMessage(Translation.of("towny.command.admin.using_plugin").replace("{plugin}","Towny"));
                    } else {
                        sender.sendMessage(Translation.of("towny.command.admin.not_using_plugin").replace("{plugin}","Towny"));
                    }

                    if (Listeners.isUsingPAPI()) {
                        sender.sendMessage(Translation.of("towny.command.admin.using_plugin").replace("{plugin}","PlaceholderAPI"));
                    } else {
                        sender.sendMessage(Translation.of("towny.command.admin.not_using_plugin").replace("{plugin}","PlaceholderAPI"));
                    }

                    if (Listeners.isUsingBungee()) {
                        sender.sendMessage(Translation.of("towny.command.admin.using_plugin").replace("{plugin}","Bungeecord"));
                    } else {
                        sender.sendMessage(Translation.of("towny.command.admin.not_using_dependency").replace("{plugin}","Bungeecord"));
                    }
                    return true;
                default:
                    sender.sendMessage(Translation.of("towny.command.admin.unknown_arg"));
                    return true;
            }
        }

        return false;
    }
}