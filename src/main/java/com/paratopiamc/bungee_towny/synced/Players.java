package com.paratopiamc.bungee_towny.synced;

import com.paratopiamc.bungee_towny.sql.SQLHost;
import com.paratopiamc.bungee_towny.sql.SQLMessage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Players {

    private SQLMessage sqlMessage;

    public Players() {
        sqlMessage = new SQLMessage(SQLHost.getCredentials());
    }

    public String getTown(String uuid) {
        try {
            ResultSet results = sqlMessage.executeSelectSQL("SELECT town FROM players WHERE uuid  = '" + uuid + "';");

            if (results.next()) {
                String value = results.getString("town");
                return value != null ? value : "townless";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "townless";
    }

    public void setTown(String town, String uuid) {
        sqlMessage.executeSQL(
                " UPDATE players" +
                        "    SET town = '" + town + "'" +
                        "WHERE uuid ='" + uuid + "';"
        );
    }

    public void setNation(String nation, String uuid) {
        sqlMessage.executeSQL(
                " UPDATE players" +
                        "    SET nation = '" + nation + "'" +
                        "WHERE uuid ='" + uuid + "';"
        );
    }

    public void setChannel(String channelName, String uuid) {
        sqlMessage.executeSQL(
                " UPDATE players" +
                        "    SET channel = '" + channelName + "'" +
                        "WHERE uuid ='" + uuid + "';"
        );
    }

    public String getNation(String uuid) {
        try {
            ResultSet results = sqlMessage.executeSelectSQL("SELECT nation FROM players WHERE uuid  = '" + uuid + "';");

            if (results.next()) {
                String value = results.getString("nation");
                return value != null ? value : "nationless";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "nationless";
    }

    public String getChannel(String uuid) {
        try {
            ResultSet results = sqlMessage.executeSelectSQL("SELECT channel FROM players WHERE uuid  = '" + uuid + "';");

            if (results.next()) {
                String value = results.getString("channel");
                return value != null ? value : "general";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "general";
    }

    public String getUUID(String name) {
        try {
            ResultSet results = sqlMessage.executeSelectSQL("SELECT uuid FROM players WHERE name  = '" + name + "';");

            if (results.next()) {
                return results.getString("uuid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getName(String uuid) {
        try {
            ResultSet results = sqlMessage.executeSelectSQL("SELECT name FROM players WHERE uuid  = '" + uuid + "';");

            if (results.next()) {
                return results.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }

    public List<String> ignoringNames(String uuid) {
        //SELECT * FROM items WHERE items.xml LIKE '%123456%'
        try {
            ResultSet results = sqlMessage.executeSelectSQL("SELECT name FROM players WHERE ignored_by LIKE '%" + uuid +"%';");

            List<String> names = new ArrayList<>();
            while (results.next()) {
                names.add(results.getString("name"));
            }
            return names;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public String[] ignoredBy(String uuid) {
        try {
            ResultSet results = sqlMessage.executeSelectSQL("SELECT ignored_by FROM players WHERE uuid = '" + uuid + "';");

            if (results.next()) {
                return results.getString("ignored_by").split(",");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new String[]{};
    }

    public String[] ignoredByNames(String uuid) {
        String[] uuids = ignoredBy(uuid);

        for (short i = 0; i < uuids.length; i++) {
            uuids[i] = getName(uuids[i]);
        }

        return uuids;
    }

    public boolean isMuted(String uuid) {
        try {
            ResultSet results = sqlMessage.executeSelectSQL("SELECT muted FROM players WHERE uuid  = '" + uuid + "';");

            if (results.next()) {
                Boolean value = results.getBoolean("muted");
                return value;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void setMuted(String uuid, boolean mute) {
        sqlMessage.executeSQL(
                " UPDATE players " +
                        "SET muted = " + mute + " " +
                        "WHERE uuid ='" + uuid + "';"
        );
    }

    public String getTitle(String uuid) {
        try {
            ResultSet results = sqlMessage.executeSelectSQL("SELECT title FROM players WHERE uuid  = '" + uuid + "';");

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
