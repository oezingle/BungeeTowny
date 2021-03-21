package com.paratopiamc.bungee_towny.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class AdminCommandTabCompletor implements TabCompleter {

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> results = new ArrayList<>();
        if (args.length == 1) {
            results.add("reload");
            results.add("status");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("reload")) {
            results.add("chat");
            results.add("messages");
            results.add("sql");
            results.add("server");
        }

        return results;
    }
}
