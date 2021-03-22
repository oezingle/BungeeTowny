package com.paratopiamc.bungee_towny.synced;

import com.paratopiamc.bungee_towny.BungeeTowny;
import com.paratopiamc.bungee_towny.sql.SQLHost;
import com.paratopiamc.bungee_towny.sql.SQLMessage;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.util.List;

public class Actions {

    private SQLMessage sqlMessage;

    public Actions() {
        sqlMessage = new SQLMessage(SQLHost.getCredentials());
    }

    public void add(String json) {
        Bukkit.getScheduler().runTaskAsynchronously(BungeeTowny.getThisPlugin(), new Runnable() {
            @Override
            public void run() {
                List<String> uuids = new Servers().allOtherServers();

                for (String server : uuids) {
                    //publish the action with that server's uuid in mind
                    sqlMessage.executeSQL("INSERT INTO action_queue (server_uuid, action) VALUES ( '" + server + "', '" + json + "');");
                }
            }
        });
    }


    //TODO finish this
    //Make sure to run this asynchronously
    public List<String> check() {
        ResultSet results = sqlMessage.executeSelectSQL("SELECT action FROM action_queue WHERE server_uuid = '" + BungeeTowny.getServerUUID() + "';");

        return null;
    }
}
