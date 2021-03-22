package com.paratopiamc.bungee_towny.listener.towny;

import com.palmergames.bukkit.towny.event.TownRemoveResidentEvent;
import com.paratopiamc.bungee_towny.BungeeTowny;
import com.paratopiamc.bungee_towny.synced.players.Players;
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
                Players players = new Players();
                //set town with sql
                players.setTown("townless", uuid);
                //also set the nation
                players.setNation("nationless", uuid);

                //TODO broadcast + action queue for telling residents that a member has left / other servers resident leaving
            }
        });

    }
}
