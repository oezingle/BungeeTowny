package com.paratopiamc.bungee_towny;

import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Set;

public abstract class Translation {

    private static String lang = "en";

    private static HashMap<String, String> translations = new HashMap<>();

    public static void setLang(String newLang) {
        //TODO check for a valid language

        lang = newLang.toLowerCase();
    }

    public static String of(String key) {
        String val = translations.get(key);

        if (val != null) {
            return val;
        } else {
            return "BUNGEETOWNY_UNSETVAL";
        }
    }

    public static void setValue(String key, String value) {
        translations.put(key, value);
    }

    public static void setFromConfig(ConfigurationSection config) {
        setFromConfig(config, "");
    }

    public static void setFromConfig(ConfigurationSection config, String prefix) {
        config = config.getConfigurationSection(lang);

        //set the keys using the configurationSection
        Set<String> keys = config.getKeys(true);
        for (String key : keys) {
            String value = config.getString(key);

            setValue(prefix + key, value);

            System.out.println(key + " | " + value);
        }
    }

}
