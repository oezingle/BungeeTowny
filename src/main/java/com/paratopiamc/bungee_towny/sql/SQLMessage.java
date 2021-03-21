package com.paratopiamc.bungee_towny.sql;

import com.paratopiamc.bungee_towny.BungeeTowny;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

/*
Much of this code is stolen verbatim from
https://www.spigotmc.org/wiki/mysql-database-integration-with-your-plugin/

I simply wrapped voids around everything, and made the class seperate from the Main class
*/
public class SQLMessage {
    public static String host, port, database, username, password;

    static Connection connection;

    private static boolean isEnabled = false;

    public static HashMap<String, Set<String>> tables;

    public SQLMessage(SQLCredentials credentials) {
        this.host = credentials.getHost();
        this.port = credentials.getPort();
        this.database = credentials.getDatabase();
        this.username = credentials.getUsername();
        this.password = credentials.getPassword();

        tables = new HashMap<>();

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true"; //Enter URL w/db name
        try { //We use a try catch to avoid errors, hopefully we don't get any.
            Class.forName("com.mysql.jdbc.Driver"); //this accesses Driver in jdbc.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("jdbc driver unavailable!");
            return;
        }
        try { //Another try catch to get any SQL errors (for example connections errors)
            connection = DriverManager.getConnection(url,username,password);
            //with the method getConnection() from DriverManager, we're trying to set
            //the connection's url, username, password to the variables we made earlier and
            //trying to get a connection at the same time. JDBC allows us to do this.
            isEnabled = true;
        } catch (SQLException e) { //catching errors)
            //e.printStackTrace(); //prints out SQLException errors to the console (if any)
            Bukkit.getLogger().log(Level.SEVERE,"=============================================");
            Bukkit.getLogger().log(Level.SEVERE,"BungeePVP cannot connect to the MySQL server!");
            Bukkit.getLogger().log(Level.SEVERE,"=============================================");

            isEnabled = false;
        }
    }

    public boolean isConnected() {
        return isEnabled;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) { //checking if connection isn't null to
                //avoid receiving a nullpointer
                connection.close(); //closing the connection field variable.
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createTable(String table_name, HashMap<String, String> table_info) {

        Set<String> keys = new HashSet<>(table_info.keySet());
        if (keys.contains("PRIMARY KEY")) {
            String primary_key = table_info.get("PRIMARY KEY");
            //TODO filter out comments!
            primary_key = primary_key.replace("(","").replace(")", "");
            keys.remove("PRIMARY KEY");
            keys.remove(primary_key);
        }

        //global to local
        this.tables.put(table_name,keys);

        String table_info_string = "";
        for (String i : table_info.keySet()) {
            table_info_string = table_info_string + i + "   " + table_info.get(i) + ",";
        }
        table_info_string = table_info_string.substring(0, table_info_string.length() - 1);

        String sql = "CREATE TABLE IF NOT EXISTS " + table_name + " (" + table_info_string + ");";

        executeSQL(sql);
    }

    public void insert(String table, String values_as_string) {

        String keyList = "";
        for (String i : tables.get(table)) {
            keyList = keyList + i + ",";
        }
        keyList = keyList.substring(0, keyList.length() - 1);

        String sql = "INSERT INTO " + table + "(" + keyList + ") VALUES " +  values_as_string + ";";

        executeSQL(sql);
    }

    public boolean select(String var_optional, String table, Optional<String> where_optional) {

        String var = var_optional.equals("") ? "*" : var_optional;

        String where = where_optional.isPresent() ? where_optional.get() : "";
        //SELECT name FROM cats WHERE owner = 'Casey';

        String sql = "SELECT " + var + " FROM " + table + " " + where + ";";

        ResultSet a = executeSelectSQL(sql);
        try {
            if (a.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void delete(String table) {
        delete(table, "");
    }

    public void delete(String table, String where) {
        String sql = "DELETE FROM " + table + " " + where + ";";

        executeSQL(sql);
    }

    public void executeSQL(String sql) {
        try {
            if (isEnabled) {
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.executeUpdate();
            } else {
                BungeeTowny.getThisPlugin().getLogger().log(Level.SEVERE, "An SQL message could not be sent!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeSelectSQL(String sql) {
        try {
            if (isEnabled) {
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet results = stmt.executeQuery();

                return results;
            } else {
                BungeeTowny.getThisPlugin().getLogger().log(Level.SEVERE, "An SQL message could not be sent!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}