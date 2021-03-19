package com.paratopiamc.bungee_towny.chat.Channel;

public enum ChannelType {
    GLOBAL,
    DEFAULT,
    NATION,
    TOWN;

    public static ChannelType fromString(String name) {
        switch (name) {
            case "GLOBAL":
                return GLOBAL;
            case "NATION":
                return NATION;
            case "TOWN":
                return TOWN;
            default:
                return DEFAULT;
        }
    }
}
