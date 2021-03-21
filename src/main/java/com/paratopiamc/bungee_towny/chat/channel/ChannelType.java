package com.paratopiamc.bungee_towny.chat.channel;

public enum ChannelType {
    GLOBAL,
    DEFAULT,
    NATION,
    TOWN,
    MESSAGE;

    public static ChannelType fromString(String name) {
        switch (name) {
            case "GLOBAL":
                return GLOBAL;
            case "NATION":
                return NATION;
            case "TOWN":
                return TOWN;
            case "MESSAGE":
                return MESSAGE;
            default:
                return DEFAULT;
        }
    }
}
