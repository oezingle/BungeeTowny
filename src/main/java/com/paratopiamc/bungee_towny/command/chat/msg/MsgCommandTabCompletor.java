package com.paratopiamc.bungee_towny.command.chat.msg;

import com.paratopiamc.bungee_towny.synced.Bungeecord;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MsgCommandTabCompletor implements TabCompleter {

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> results = new ArrayList<>();
        if (args.length == 1) {
            //add the players
            results = Bungeecord.getPlayers();

            if (results.size() == 0) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    results.add(player.getName());
                }
            }
        }
        return results;
    }
}
