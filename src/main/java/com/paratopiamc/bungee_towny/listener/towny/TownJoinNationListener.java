package com.paratopiamc.bungee_towny.listener.towny;

import com.palmergames.bukkit.towny.event.NationAddTownEvent;
import com.paratopiamc.bungee_towny.sql.SQLHost;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownJoinNationListener implements Listener {

    @EventHandler
    public void onTownJoinNation(NationAddTownEvent event) {
        String town = event.getTown().getName();
        String nation = event.getNation().getName();

        //set nation with sql
        SQLHost.getMessenger().executeSQL(
                " UPDATE players" +
                        "    SET nation = '" + nation + "'" +
                        "WHERE town ='" + town + "';"
        );
    }
}
