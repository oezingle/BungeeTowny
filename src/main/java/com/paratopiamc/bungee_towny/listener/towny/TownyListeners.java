package com.paratopiamc.bungee_towny.listener.towny;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class TownyListeners {
    private Plugin plugin;

    private TownJoinListener townJoinListener;
    private TownLeaveListener townLeaveListener;

    private TownJoinNationListener townJoinNationListener;
    private TownLeaveNationListener townLeaveNationListener;

    public TownyListeners(Plugin plugin) {
        this.plugin = plugin;

        this.reload();
    }

    public void reload() {
        townJoinListener = new TownJoinListener();
        townLeaveListener = new TownLeaveListener();

        townJoinNationListener = new TownJoinNationListener();
        townLeaveNationListener = new TownLeaveNationListener();

        Bukkit.getPluginManager().registerEvents(townJoinListener, plugin);
        Bukkit.getPluginManager().registerEvents(townLeaveListener, plugin);

        Bukkit.getPluginManager().registerEvents(townJoinNationListener, plugin);
        Bukkit.getPluginManager().registerEvents(townLeaveNationListener, plugin);

    }
}
