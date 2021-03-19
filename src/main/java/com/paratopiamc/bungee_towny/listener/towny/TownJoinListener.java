package com.paratopiamc.bungee_towny.listener.towny;

import com.palmergames.bukkit.towny.event.TownAddResidentEvent;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.paratopiamc.bungee_towny.synced.Players;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class TownJoinListener implements Listener {

    @EventHandler
    public void onTownJoin(TownAddResidentEvent event) {
        //System.out.println(event.getEventName());
        String uuid = event.getResident().getUUID().toString();

        String town = "townless";
        String nation = "nationless";
        try {
            town = event.getResident().getTown().getName();
        } catch (NotRegisteredException e) {
            e.printStackTrace();

            System.err.println("THIS IS SERIOUS");
        }
        //set town with sql
        Players.setTown(town, uuid);

        try {
            nation = event.getResident().getTown().getNation().getName();
        } catch (NotRegisteredException e) {}
        //set nation with sql
        Players.setNation(nation, uuid);
    }
}
