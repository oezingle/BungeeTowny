package com.paratopiamc.bungee_towny.synced;

import com.paratopiamc.bungee_towny.BungeeTowny;
import com.paratopiamc.bungee_towny.sql.SQLHost;
import com.paratopiamc.bungee_towny.sql.SQLMessage;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Servers {

    public String getServerByName(String name) {
        ResultSet result = new SQLMessage(SQLHost.getCredentials()).executeSelectSQL("SELECT server_uuid FROM servers WHERE server_name = '" + name + "';");

        try {
            while (result.next()) {
                String uuid = result.getString("server_uuid");

                return uuid;
            }
        } catch (SQLException e) {
            e.printStackTrace();

            System.out.println("[BungeeTowny] An unexpected SQL error has occurred");
        }

        //fallback value
        return null;
    }

    public String getServerByUUID(String uuid) {
        ResultSet result = new SQLMessage(SQLHost.getCredentials()).executeSelectSQL("SELECT server_name FROM servers WHERE server_uuid = '" + uuid + "';");

        try {
            while (result.next()) {
                String name = result.getString("server_name");

                return name;
            }
        } catch (SQLException e) {
            e.printStackTrace();

            System.out.println("[BungeeTowny] An unexpected SQL error has occurred");
        }

        //fallback value
        return null;
    }

}
