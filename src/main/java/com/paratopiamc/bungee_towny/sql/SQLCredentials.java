package com.paratopiamc.bungee_towny.sql;

import org.bukkit.configuration.ConfigurationSection;

public class SQLCredentials {

    private String host;
    private String port;
    private String database;
    private String username;
    private String password;

    public SQLCredentials(String host, String port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public static SQLCredentials fromConfigSection(ConfigurationSection mysql) {
        String host = mysql.getString("host");
        String port = mysql.getString("port");
        String database = mysql.getString("database");
        String username = mysql.getString("username");
        String password = mysql.getString("password");

        return new SQLCredentials(host, port, database, username, password);
    }

    public String getDatabase() {
        return database;
    }

    public String getHost() {
        return host;
    }

    public String getPassword() {
        return password;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }
}
