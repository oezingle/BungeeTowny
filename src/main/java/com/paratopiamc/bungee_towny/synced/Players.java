package com.paratopiamc.bungee_towny.synced;

import com.paratopiamc.bungee_towny.BungeeTowny;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Players {
    public static String getTown(String uuid) {
        try {
            ResultSet results = BungeeTowny.sqlhost.getMessenger().executeSelectSQL("SELECT town FROM players WHERE uuid  = '" + uuid + "';");

            if (results.next()) {
                String value = results.getString("town");
                return value != null ? value : "townless";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "townless";
    }

    public static void setTown(String town, String uuid) {
        BungeeTowny.sqlhost.getMessenger().executeSQL(
                " UPDATE players" +
                        "    SET town = '" + town + "'" +
                        "WHERE uuid ='" + uuid + "';"
        );
    }

    public static void setNation(String nation, String uuid) {
        BungeeTowny.sqlhost.getMessenger().executeSQL(
                " UPDATE players" +
                        "    SET nation = '" + nation + "'" +
                        "WHERE uuid ='" + uuid + "';"
        );
    }

    public static void setChannel(String channelName, String uuid) {
        BungeeTowny.sqlhost.getMessenger().executeSQL(
                " UPDATE players" +
                        "    SET channel = '" + channelName + "'" +
                        "WHERE uuid ='" + uuid + "';"
        );
    }

    public static String getNation(String uuid) {
        try {
            ResultSet results = BungeeTowny.sqlhost.getMessenger().executeSelectSQL("SELECT nation FROM players WHERE uuid  = '" + uuid + "';");

            if (results.next()) {
                String value = results.getString("nation");
                return value != null ? value : "nationless";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "nationless";
    }

    public static String getChannel(String uuid) {
        try {
            ResultSet results = BungeeTowny.sqlhost.getMessenger().executeSelectSQL("SELECT channel FROM players WHERE uuid  = '" + uuid + "';");

            if (results.next()) {
                String value = results.getString("channel");
                return value != null ? value : "general";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "general";
    }

    public static boolean isMuted(String uuid) {
        try {
            ResultSet results = BungeeTowny.sqlhost.getMessenger().executeSelectSQL("SELECT muted FROM players WHERE uuid  = '" + uuid + "';");

            if (results.next()) {
                Boolean value = results.getBoolean("muted");
                return value;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void setMuted(String uuid, boolean mute) {
        BungeeTowny.sqlhost.getMessenger().executeSQL(
                " UPDATE players" +
                        "    SET muted = " + mute + "" +
                        "WHERE uuid ='" + uuid + "';"
        );
    }

    public static String getTitle(String uuid) {
        try {
            ResultSet results = BungeeTowny.sqlhost.getMessenger().executeSelectSQL("SELECT title FROM players WHERE uuid  = '" + uuid + "';");

            if (results.next()) {
                String value = results.getString("title");
                return value != null ? value + " ": "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }
}
