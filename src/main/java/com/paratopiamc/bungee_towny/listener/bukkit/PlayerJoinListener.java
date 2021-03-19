package com.paratopiamc.bungee_towny.listener.bukkit;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Resident;
import com.paratopiamc.bungee_towny.BungeeTowny;
import com.paratopiamc.bungee_towny.listener.Listeners;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        String uuid = player.getUniqueId().toString();
        String name = player.getName();
        //uuid|name|town|channel|nation|title|res
        //create a player in the sql database
        boolean alreadyExists = false;
        try {
            ResultSet results = BungeeTowny.sqlhost.getMessenger().executeSelectSQL("SELECT * FROM players WHERE uuid = '" + uuid + "'");

            alreadyExists = results.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!alreadyExists) {
            BungeeTowny.sqlhost.getMessenger().executeSQL(
                    "INSERT INTO players (uuid, name, town, channel, nation, title, res, muted) VALUES (\"" + uuid + "\",\"" + name + "\",null,\"" + "general" + "\", null, null, null, false);"
            );

        }

        //update the username
        BungeeTowny.sqlhost.getMessenger().executeSQL(
                " UPDATE players" +
                        "    SET name = '" + name + "'" +
                        "WHERE uuid ='" + uuid + "';"
        );

        //update the title and res if towny is around
        if (Listeners.isUsingTowny()) {
            Resident resident = TownyUniverse.getInstance().getResident(player.getUniqueId());
            String title = resident.getTitle();

            BungeeTowny.sqlhost.getMessenger().executeSQL(
                    " UPDATE players" +
                            "    SET title = '" + title + "'" +
                            "WHERE uuid ='" + uuid + "';"
            );


            /*List<String> friends = new ArrayList<>();
            for (Resident friend : resident.getFriends()) {
                friends.add(friend.getName());
            }

            Long lastOnline = resident.getLastOnline();*/
        }

        //check the queue for actions on this player

    }
}
