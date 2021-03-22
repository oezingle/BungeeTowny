package com.paratopiamc.bungee_towny.synced;

import com.paratopiamc.bungee_towny.BungeeTowny;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public abstract class Bungeecord {

    private static List<String> players = new ArrayList<>();

    public static void setPlayerList(String[] newList) {
        Bukkit.getScheduler().runTaskAsynchronously(BungeeTowny.getThisPlugin(), new Runnable() {
            @Override
            public void run() {
                List<String> playerList = new ArrayList<>();

                for (String player : newList) {
                    playerList.add(player);
                }
                players = playerList;
            }
        });
    }

    public static List<String> getPlayers() {
        return players;
    }
}
