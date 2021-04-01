package com.paratopiamc.bungee_towny.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class AdminCommandTabCompletor implements TabCompleter {

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> results = new ArrayList<>();
        if (sender.hasPermission("bungeetowny.admin")) {
            if (args.length == 1) {
                if (sender.hasPermission("bungeetowny.admin.reload"))
                    results.add("reload");
                if (sender.hasPermission("bungeetowny.admin.status"))
                    results.add("status");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission("bungeetowny.admin.reload")) {
                        results.add("chat");
                        results.add("messages");
                        results.add("sql");
                        results.add("server");
                    }
                } else if (args[0].equalsIgnoreCase("status")) {
                    if (sender.hasPermission("bungeetowny.admin.status")) {
                        results.add("chat");
                    }
                }
            }
        }
        return results;
    }
}
