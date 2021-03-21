package com.paratopiamc.bungee_towny.listener;

import com.paratopiamc.bungee_towny.listener.bukkit.ChatListener;
import com.paratopiamc.bungee_towny.listener.bukkit.PlayerJoinListener;
import com.paratopiamc.bungee_towny.listener.bukkit.PlayerLeaveListener;
import com.paratopiamc.bungee_towny.listener.towny.TownyListeners;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public abstract class Listeners {

    private static Plugin plugin;

    private static PlayerJoinListener joinListener;
    private static PlayerLeaveListener leaveListener;
    private static ChatListener chatListener;

    private static TownyListeners townyListeners;

    private static boolean usingChat = false;
    private static boolean usingTowny = false;

    private static boolean usingPAPI = false;
    private static boolean usingBungee = false;

    public static void init(Plugin newPlugin) {

        plugin = newPlugin;

        reload();
    }

    public static void usingChat(boolean using) {
        usingChat = using;

        reload();
    }

    public static void usingPAPI(boolean using) {
        usingPAPI = using;
        //chatListener.usingPAPI(using);
    }

    public static void usingTowny(boolean using) {
        usingTowny = using;

        //chatListener.usingTowny(using);
        reload();
    }

    public static void usingBungee(boolean using) {
        usingBungee = using;

        //chatListener.usingBungee(using);
    }

    public static void reload() {
        if (plugin == null) {
            return;
        }

        joinListener = new PlayerJoinListener();

        Bukkit.getPluginManager().registerEvents(joinListener, plugin);

        //optional listeners
        if (usingChat) {
            chatListener = new ChatListener();
            Bukkit.getPluginManager().registerEvents(chatListener, plugin);
        } else {
            chatListener = null;
        }

        //more optional listeners
        if (usingTowny && townyListeners == null) {
            //create the towny listeners
            townyListeners = new TownyListeners(plugin);
        }
        if (usingTowny) {
            //reload the towny listeners
            townyListeners.reload();

            leaveListener = new PlayerLeaveListener();
            Bukkit.getPluginManager().registerEvents(leaveListener, plugin);
        } else {
            townyListeners = null;
        }
    }

    public static void unload() {
        chatListener = null;

        joinListener = null;
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static boolean isUsingBungee() {
        return usingBungee;
    }

    public static boolean isUsingChat() {
        return usingChat;
    }

    public static boolean isUsingPAPI() {
        return usingPAPI;
    }

    public static boolean isUsingTowny() {
        return usingTowny;
    }
}
