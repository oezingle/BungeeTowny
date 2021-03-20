package com.paratopiamc.bungee_towny.synced;

import com.paratopiamc.bungee_towny.sql.SQLHost;
import com.paratopiamc.bungee_towny.sql.SQLMessage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class Nations {

    public List<String> getResidents(String nation) {
        try {
            ResultSet results = new SQLMessage(SQLHost.getCredentials()).executeSelectSQL("SELECT uuid FROM players WHERE nation  = '" + nation + "';");

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

    public static List<String> getResidentNames(String nation) {
        try {
            ResultSet results = new SQLMessage(SQLHost.getCredentials()).executeSelectSQL("SELECT name FROM players WHERE nation  = '" + nation + "';");

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
