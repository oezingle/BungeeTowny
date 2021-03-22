package com.paratopiamc.bungee_towny.listener.towny;

import com.palmergames.bukkit.towny.event.TownRemoveResidentEvent;
import com.paratopiamc.bungee_towny.BungeeTowny;
import com.paratopiamc.bungee_towny.synced.Players;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownLeaveListener implements Listener {

    @EventHandler
    public void onTownLeave(TownRemoveResidentEvent event) {
        String uuid = event.getResident().getUUID().toString();

        Bukkit.getScheduler().runTaskAsynchronously(BungeeTowny.getThisPlugin(), new Runnable() {
            @Override
            public void run() {
                //set town with sql
                Players.setTown("townless", uuid);
                //also set the nation
                Players.setNation("nationless", uuid);

                //TODO broadcast + action queue for telling residents that a member has left / other servers resident leaving
            }
        });

    }
}
