package com.paratopiamc.bungee_towny.synced;

import com.paratopiamc.bungee_towny.sql.SQLHost;
import com.paratopiamc.bungee_towny.sql.SQLMessage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Towns {

    private SQLMessage sqlMessage;

    public Towns() {
        sqlMessage = new SQLMessage(SQLHost.getCredentials());
    }

    public List<String> getResidents(String town) {
        try {
            ResultSet results = new SQLMessage(SQLHost.getCredentials()).executeSelectSQL("SELECT uuid FROM players WHERE town  = '" + town + "';");

            List<String> list = new ArrayList<>();

            while (results.next()) {
                list.add(results.getString("uuid"));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<String> getAllNames() {
        try {
            ResultSet results = new SQLMessage(SQLHost.getCredentials()).executeSelectSQL("SELECT name FROM players;");

            List<String> list = new ArrayList<>();

            while (results.next()) {
                list.add(results.getString("name"));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<String> getAllUUIDs() {
        try {
            ResultSet results = new SQLMessage(SQLHost.getCredentials()).executeSelectSQL("SELECT uuid FROM players;");

            List<String> list = new ArrayList<>();

            while (results.next()) {
                list.add(results.getString("uuid"));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<String> getResidentNames(String town) {
        try {
            ResultSet results = new SQLMessage(SQLHost.getCredentials()).executeSelectSQL("SELECT name FROM players WHERE town  = '" + town + "';");

            List<String> list = new ArrayList<>();

            while (results.next()) {
                list.add(results.getString("name"));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
