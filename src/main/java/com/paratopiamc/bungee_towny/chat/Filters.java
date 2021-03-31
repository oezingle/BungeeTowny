package com.paratopiamc.bungee_towny.chat;

import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;


/*
This class basically just
 */
public abstract class Filters {

    //                      name           filter  enabled
    private static HashMap<String, HashMap<String, Boolean>> channels;

    //TODO warnings

    public void init(ConfigurationSection filters) {
        channels = new HashMap<>();


        ConfigurationSection swearSection = filters.getConfigurationSection("swear_filter");
        if (swearSection.getBoolean("enabled")) {
            initSection(swearSection, swearSection.getBoolean("enabled"));
        }
    }

    private void initSection(ConfigurationSection section, boolean enable) {
        List<String> thisSectionChannels = section.getStringList("channels");
        for (String channel : thisSectionChannels) {
            HashMap thisChannel = channels.get("channel");
            if (thisChannel == null) {
                thisChannel = new HashMap();
            }

            //now we can put if that filter is enabled in the hashmap
            thisChannel.put(section.getName(), enable);
        }
    }

}


/*filter:
        warnings:
        enabled: true
        #at 3 warnings, the player will be automatically temp muted
        count: 3

        swear_filter:
        enabled: true
        #the list of channels for this filter to moderate
        channels: [ALL]
        #If false, messages from swearing players will be withheld
        replace: true
        #default | per_player
        type: default
    #how many warning 'points' this is worth
            warning: 1

            #disable unicode fonts often found in hacked clients
            fancychat_filter:
            enabled: true
            #the list of channels for this filter to moderate
            channels: [ALL]
            notify_staff: true
            #how many warning 'points' this is worth
            warning: 1

            link_filter:
            enabled: true
            #the list of channels for this filter to moderate
            channels: [ALL]
            notify_staff: false
            #list of websites that won't be blocked.
            allowed:
            - "discord.gg"
            - "youtube.com"
            #how many warning 'points' this is worth
            warning: 1*/