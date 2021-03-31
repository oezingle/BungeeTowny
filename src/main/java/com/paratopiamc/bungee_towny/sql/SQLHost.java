package com.paratopiamc.bungee_towny.sql;

import com.paratopiamc.bungee_towny.BungeeTowny;
import com.paratopiamc.bungee_towny.listener.Listeners;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

//TODO consider an abstract class here, with an init() function

public abstract class SQLHost {

    private static SQLCredentials credentials;

    private static SQLMessage messenger;

    public static void init(ConfigurationSection mysql) {

        credentials = SQLCredentials.fromConfigSection(mysql);

        messenger = new SQLMessage(credentials);

        //server_uuid | server_name
        HashMap<String, String> table_info = new HashMap<>();
        table_info.put("server_uuid", "VARCHAR(36) NOT NULL");
        //                             this length is arbitrary, idk what to make it
        table_info.put("server_name", "VARCHAR(64) NOT NULL");
        table_info.put("using_towny", "BOOLEAN DEFAULT FALSE");
        messenger.createTable("servers", table_info);

        //player | town | nation | title | res info?
        table_info = new HashMap<>();
        table_info.put("uuid", "VARCHAR(36) NOT NULL");
        table_info.put("name", "VARCHAR(16) NOT NULL");
        table_info.put("ignored_by", "TEXT NOT NULL DEFAULT ''");
        table_info.put("town", "VARCHAR(36) DEFAULT null");
        table_info.put("channel", "VARCHAR(36) NOT NULL DEFAULT 'general'");
        table_info.put("nation", "VARCHAR(36) DEFAULT null");
        table_info.put("title", "VARCHAR(10) DEFAULT null");
        table_info.put("res", "JSON DEFAULT null");
        table_info.put("muted", "BOOLEAN DEFAULT FALSE");
        messenger.createTable("players", table_info);

        //TODO tempmutes
        /*
        //add a table if it's using chat, to enable tempmuting
        if (Listeners.isUsingChat()) {
            table_info = new HashMap<>();
            table_info.put("uuid", "VARCHAR(36) NOT NULL");
            table_info.put("unmute_date", "DATETIME");
            messenger.createTable("tempmutes", table_info);
        }
         */

        //town | nation | color | allies | claims
        table_info = new HashMap<>();
        table_info.put("town", "VARCHAR(36) NOT NULL");
        table_info.put("nation", "VARCHAR(36) NOT NULL");
        table_info.put("color", "VARCHAR(10) NOT NULL");
        table_info.put("info", "JSON");
        table_info.put("allies_enemies", "JSON");
        table_info.put("claims", "JSON");
        messenger.createTable("towns", table_info);

        //nation | towns | color | allies
        table_info = new HashMap<>();
        table_info.put("nation", "VARCHAR(36) NOT NULL");
        table_info.put("towns", "JSON");
        table_info.put("color", "VARCHAR(10) NOT NULL");
        table_info.put("allies_enemies", "JSON");
        table_info.put("info", "JSON");
        messenger.createTable("nations", table_info);

        //town | players | allies
        /*table_info = new HashMap<>();
        table_info.put("town", "VARCHAR(36) NOT NULL");
        table_info.put("players", "JSON");
        messenger.createTable("town_players", table_info);*/

        //queue - server_uuid | action
        table_info = new HashMap<>();
        table_info.put("server_uuid", "VARCHAR(36) NOT NULL");
        //table_info.put("type", "VARCHAR(10) NOT NULL");
        table_info.put("action", "JSON");
        messenger.createTable("action_queue", table_info);

        //town | server_uuid | spawn/outpost
        table_info = new HashMap<>();
        table_info.put("server_uuid", "VARCHAR(36) NOT NULL");
        table_info.put("town_or_nation", "VARCHAR(38) NOT NULL");            //t:town | n:nation
        table_info.put("outpost_number", "TINYINT unsigned NOT NULL");
        messenger.createTable("spawnpoints", table_info);

        //config merge
        table_info = new HashMap<>();
        table_info.put("date", "DATE NOT NULL");
        table_info.put("config", "JSON");
        messenger.createTable("config", table_info);
        //automate date
        //messenger.executeSQL("ALTER TABLE config MODIFY date datetime DEFAULT CURRENT_TIMESTAMP;");
        //remove table value
        //messenger.tables.get("config").remove("date");

    }

    public static void set_server(String uuid, String name) {
        //delete the old entry (if there is one)
        messenger.delete("servers","WHERE server_uuid = '" + uuid + "';");

        messenger.executeSQL("INSERT INTO servers (server_name, server_uuid) VALUES ('" + name + "', '" + uuid + "')");

        if (Listeners.isUsingTowny()) {
            //set that bit to true
            messenger.executeSQL(
                    " UPDATE servers SET using_towny = true WHERE server_uuid ='" + BungeeTowny.getServerUUID() + "';"
            );
        }
    }

    public static void set_config(String key, File config) {
        byte[] file = null;

        try {
            file = Files.readAllBytes(config.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println(file);

    }

    public static SQLCredentials getCredentials() {
        return credentials;
    }

    public static SQLMessage getMessenger() {
        return messenger;
    }

}

/*
        host = "localhost";
        port = "3306";
        database = "TestDatabase";
        username = "user";
        password = "pass";
 */