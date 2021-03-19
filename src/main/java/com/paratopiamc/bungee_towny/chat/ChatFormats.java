package com.paratopiamc.bungee_towny.chat;

import org.bukkit.configuration.ConfigurationSection;

public class ChatFormats {
    private static String world;
    private static String town;
    private static String nation;
    private static String both;

    public static void init(ConfigurationSection section) {
        world = section.getString("world");
        town = section.getString("town");
        nation = section.getString("nation");
        both = section.getString("both");
    }

    public static String getBoth() {
        return both;
    }

    public static String getNation() {
        return nation;
    }

    public static String getTown() {
        return town;
    }

    public static String getWorld() {
        return world;
    }

    public static String toTown(String in) {
        if (in == "") {
            return "";
        }

        return town.replace("%s", in);
    }

    public static String toNation(String in) {
        if (in == "") {
            return "";
        }

        return nation.replace("%s", in);
    }
}
