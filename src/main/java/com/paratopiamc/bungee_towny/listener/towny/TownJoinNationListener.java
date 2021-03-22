package com.paratopiamc.bungee_towny.listener.towny;

import com.palmergames.bukkit.towny.event.NationAddTownEvent;
import com.paratopiamc.bungee_towny.BungeeTowny;
import com.paratopiamc.bungee_towny.sql.SQLHost;
import com.paratopiamc.bungee_towny.sql.SQLMessage;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownJoinNationListener implements Listener {

    @EventHandler
    public void onTownJoinNation(NationAddTownEvent event) {
        String town = event.getTown().getName();
        String nation = event.getNation().getName();

        //set nation with sql
        Bukkit.getScheduler().runTaskAsynchronously(BungeeTowny.getThisPlugin(), new Runnable() {
            @Override
            public void run() {
                new SQLMessage(SQLHost.getCredentials()).executeSQL(
                        " UPDATE players" +
                                "    SET nation = '" + nation + "'" +
                                "WHERE town ='" + town + "';"
                );
            }
        });
    }
}
