package com.paratopiamc.bungee_towny.chat;

import org.bukkit.configuration.ConfigurationSection;

public abstract class ChatColors {
    private static String kingColor;
    private static String mayorColor;
    private static String residentColor;
    private static String nomadColor;

    public static void init(ConfigurationSection section) {
        kingColor = section.getString("king");
        mayorColor = section.getString("mayor");
        residentColor = section.getString("resident");
        nomadColor = section.getString("nomad");
    }

    public static String getKingColor() {
        return kingColor;
    }

    public static String getMayorColor() {
        return mayorColor;
    }

    public static String getNomadColor() {
        return nomadColor;
    }

    public static String getResidentColor() {
        return residentColor;
    }
}
