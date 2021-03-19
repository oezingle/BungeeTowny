package com.paratopiamc.bungee_towny.listener.bukkit;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Resident;
import com.paratopiamc.bungee_towny.BungeeTowny;
import com.paratopiamc.bungee_towny.listener.Listeners;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();

        if (Listeners.isUsingTowny()) {
            Resident resident = TownyUniverse.getInstance().getResident(player.getUniqueId());
            String title = resident.getTitle();

            BungeeTowny.sqlhost.getMessenger().executeSQL(
                    " UPDATE players" +
                            "    SET title = '" + title + "'" +
                            "WHERE uuid ='" + uuid + "';"
            );
        }
    }
}
