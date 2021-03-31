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

    public static void reset() {
        translations = new HashMap<>();
    }

    //translation with replaced colors and a not-null value
    public static String of(String key) {
        String val = translations.get(key);

        if (val != null) {
            return val.replace("&", "\u00a7");
        } else {
            return "&cBUNGEETOWNY_UNSETVAL".replace("&", "\u00a7");
        }
    }

    //raw translation
    public static String get(String key) {
        return translations.get(key);
    }

    public static void setValue(String key, String value) {
        translations.put(key, value);
    }

    public static void setFromConfig(ConfigurationSection config) {
        setFromConfig(config, "");
    }

    public static void setFromConfig(ConfigurationSection config, String prefix) {
        try {
            config = config.getConfigurationSection(lang);

            addKeys(config, prefix);
        } catch (Exception e) {
            BungeeTowny.getThisPlugin().getLogger().info("The " + prefix + " message file does not have your language");
        }
    }

    public static void addKeys(ConfigurationSection config, String prefix) {
        try {
            if (prefix != "" && !prefix.endsWith(".")) {
                prefix = prefix + ".";
            }

            //set the keys using the configurationSection
            Set<String> keys = config.getKeys(true);
            for (String key : keys) {
                String value = config.getString(key);

                setValue(prefix + key, value);

                //BungeeTowny.getThisPlugin().getLogger().info(prefix + key + " | " + value);
            }
        } catch (Exception e) {
            BungeeTowny.getThisPlugin().getLogger().info("The " + prefix + " message file does not have your language");
        }
    }

}
