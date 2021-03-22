package com.paratopiamc.bungee_towny.listener.towny;

import com.palmergames.bukkit.towny.event.TownAddResidentEvent;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.paratopiamc.bungee_towny.BungeeTowny;
import com.paratopiamc.bungee_towny.synced.Players;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class TownJoinListener implements Listener {

    @EventHandler
    public void onTownJoin(TownAddResidentEvent event) {
        String uuid = event.getResident().getUUID().toString();


        //TODO the whole event living in double try-catch blocks is stupid
        try {
            String town = event.getResident().getTown().getName();

            Bukkit.getScheduler().runTaskAsynchronously(BungeeTowny.getThisPlugin(), new Runnable() {
                @Override
                public void run() {
                    //set town with sql
                    Players players = new Players();

                    players.setTown(town, uuid);

                    try {
                        String nation = event.getResident().getTown().getNation().getName();

                        //set nation with sql
                        players.setNation(nation, uuid);
                    } catch (NotRegisteredException e) {

                    }

                    //TODO add to the queue
                    ///ta town test add zingle_
                }
            });
        } catch (NotRegisteredException e) {
            e.printStackTrace();

            System.err.println("THIS IS SERIOUS");
        }
    }
}
