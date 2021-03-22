package com.paratopiamc.bungee_towny.synced;

import com.paratopiamc.bungee_towny.BungeeTowny;
import com.paratopiamc.bungee_towny.sql.SQLHost;
import com.paratopiamc.bungee_towny.sql.SQLMessage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Servers {

    private SQLMessage sqlMessage;

    public Servers() {
        sqlMessage = sqlMessage;
    }

    public String getServerByName(String name) {
        ResultSet result = sqlMessage.executeSelectSQL("SELECT server_uuid FROM servers WHERE server_name = '" + name + "';");

        try {
            while (result.next()) {
                String uuid = result.getString("server_uuid");

                return uuid;
            }
        } catch (SQLException e) {
            e.printStackTrace();


            BungeeTowny.getThisPlugin().getLogger().log(Level.WARNING, "An unexpected SQL error has occurred");
        }

        //fallback value
        return null;
    }

    public String getServerByUUID(String uuid) {
        ResultSet result = sqlMessage.executeSelectSQL("SELECT server_name FROM servers WHERE server_uuid = '" + uuid + "';");

        try {
            while (result.next()) {
                String name = result.getString("server_name");

                return name;
            }
        } catch (SQLException e) {
            e.printStackTrace();

            BungeeTowny.getThisPlugin().getLogger().log(Level.WARNING, "An unexpected SQL error has occurred");
        }

        //fallback value
        return null;
    }

    public List<String> allOtherServers() {
        ResultSet result = sqlMessage.executeSelectSQL("SELECT server_uuid FROM servers WHERE server_uuid != '" + BungeeTowny.getServerUUID() + "';");

        try {
            List<String> uuids = new ArrayList<>();
            while(result.next()) {
                uuids.add(result.getString("server_uuid"));
            }
            return uuids;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //this value shouldn't be reached
        return null;
    }

}
