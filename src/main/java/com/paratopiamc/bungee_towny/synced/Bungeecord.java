package com.paratopiamc.bungee_towny.synced;

import java.util.ArrayList;
import java.util.List;

public abstract class Bungeecord {

    private static List<String> players = new ArrayList<>();

    public static void setPlayerList(String[] newList) {
        List<String> playerList = new ArrayList<>();

        for (String player : players) {
            playerList.add(player);
        }
        players = playerList;
    }

    public static List<String> getPlayers() {
        return players;
    }
}
