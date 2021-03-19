package com.paratopiamc.bungee_towny.listener.towny;

import com.palmergames.bukkit.towny.event.nation.NationTownLeaveEvent;
import com.palmergames.bukkit.towny.object.Resident;
import com.paratopiamc.bungee_towny.BungeeTowny;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class TownLeaveNationListener implements Listener {

    @EventHandler
    public void onTownLeaveNation(NationTownLeaveEvent event) {
        String town = event.getTown().getName();

        //set nation with sql
        BungeeTowny.sqlhost.getMessenger().executeSQL(
                " UPDATE players" +
                        "    SET nation = 'nationless'" +
                        "WHERE town ='" + town + "';"
        );

        List<Resident> residents = event.getTown().getResidents();

        for (Resident resident : residents) {
            Player player = resident.getPlayer();

            //TODO This is stupid
            Bukkit.dispatchCommand(player, "chat general");
        }
    }
}
