package com.paratopiamc.bungee_towny.chat;

import com.paratopiamc.bungee_towny.Translation;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;


/*
This class basically only has static methods for checking on filters and their configuration
 */
public abstract class Filters {

    //                     channel         filter  enabled
    private static HashMap<String, HashMap<String, Boolean>> channels;
    //                     channel         filter  warning count
    private static HashMap<String, HashMap<String, Integer>> warnings;

    private static Boolean swearReplace;
    private static Integer capsMinPercent;
    private static Integer capsMinLetters;
    private static Integer repeatHistorySize;

    private static List<String> websiteAllowed;


    //TODO warnings

    public static void init(ConfigurationSection filters) {
        channels = new HashMap<>();

        ConfigurationSection swearSection = filters.getConfigurationSection("swear_filter");
        initSection(swearSection);
        swearReplace = swearSection.getBoolean("replace");

        ConfigurationSection fancyChatSection = filters.getConfigurationSection("fancychat_filter");
        initSection(fancyChatSection);

        ConfigurationSection capsSection = filters.getConfigurationSection("caps_filter");
        initSection(capsSection);
        capsMinPercent = capsSection.getInt("percent");
        capsMinLetters = capsSection.getInt("min_letters");

        ConfigurationSection repeatSection = filters.getConfigurationSection("repeat_filter");
        initSection(repeatSection);
        repeatHistorySize = repeatSection.getInt("history");

        ConfigurationSection websiteSection = filters.getConfigurationSection("link_filter");
        initSection(websiteSection);
        websiteAllowed = websiteSection.getStringList("allowed");
    }

    private static void initSection(ConfigurationSection section) {
        boolean enable = section.getBoolean("enabled");

        //pretty easy way to minimize issues
        if (enable) {
            List<String> thisSectionChannels = section.getStringList("channels");
            for (String channel : thisSectionChannels) {
                channel = channel.toLowerCase();

                HashMap thisChannel = channels.get(channel);
                if (thisChannel == null) {
                    channels.put(channel, new HashMap());
                    thisChannel = channels.get(channel);
                }

                //now we can put if that filter is enabled in the hashmap
                thisChannel.put(section.getName(), true);
            }
        }
    }

    public static boolean isFilterEnabled(String channelName, String filterName) {

        HashMap<String, Boolean> allChannel = channels.get("all");


        HashMap<String, Boolean> channel = channels.get(channelName);
        if (channel == null)
            return false;

        Boolean enabled = channel.get(filterName);
        if (enabled == null)
            return false;

        return channel.get(filterName);
    }

    public static List<String> filtersEnabled() {
        List<String> filtersEnabled = new ArrayList<>();

        if (channels == null) {
            return filtersEnabled;
        }

        /*for (String channelName : channels.keySet()) {
            HashMap<String, Boolean> filters = channels.get(channelName);

            List<String> filterList = new ArrayList<>(filters.keySet());
            for (short i = 0; i < filterList.size(); i++) {
                String filterName = filterList.get(i);

                if (filters.get(filterName)) {
                    if (filtersEnabled.get(i) == null) {
                        //new string
                        filtersEnabled.set(i, filterName + " ");
                    }

                    //add this actual channel
                    filtersEnabled.set(i, filtersEnabled.get(i) + " " + channelName);
                }
            }
        }*/

        //for every channel
        for (String channelName : channels.keySet()) {
            HashMap<String, Boolean> filters = channels.get(channelName);

            String filterOut = channelName.toUpperCase() + ": ";

            //for every filter
            for (String filter : filters.keySet()) {
                String filterFormatted = Translation.get("chat.admin.status.filter." + filter);
                filterFormatted = filterFormatted == null ? filter : filterFormatted;

                filterOut += (filters.get(filter) ? "&a" : "&e").replace("&", "\u00a7") + filterFormatted + " ";
            }

            filtersEnabled.add(filterOut);
        }

        return filtersEnabled;
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