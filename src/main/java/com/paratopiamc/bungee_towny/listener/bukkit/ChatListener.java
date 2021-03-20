package com.paratopiamc.bungee_towny.listener.bukkit;

import com.paratopiamc.bungee_towny.chat.channel.Channel;
import com.paratopiamc.bungee_towny.chat.Channels;
import com.paratopiamc.bungee_towny.chat.ChatSendEvent;
import com.paratopiamc.bungee_towny.synced.Players;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class ChatListener implements Listener {

    private static boolean usingTowny = false;
    private static boolean usingBungee = false;
    private static boolean usingPAPI = false;

    @EventHandler(priority = EventPriority.MONITOR)
    void onPlayerChat(AsyncPlayerChatEvent event) {

        //mute detection
        if (!event.isCancelled()) {
            Player player = event.getPlayer();

            String uuid = player.getUniqueId().toString();

            String channelName = Players.getChannel(uuid);

            Channel channel = Channels.get(channelName);

            String playerMessage = event.getMessage();

            ChatSendEvent chatEvent = new ChatSendEvent(playerMessage, channel, player, !Bukkit.isPrimaryThread());

            Bukkit.getPluginManager().callEvent(chatEvent);
        }

        //event.setMessage("");

        event.setCancelled(true);
    }

    /*private String replaceColors(String message) {
        return message.replace("&", "\u00a7");
    }

    private String getNamePostfix(Resident resident) {

        if (resident == null)
            return "";
        if (resident.isKing())
            return TownySettings.getKingPostfix(resident);
        else if (resident.isMayor())
            return TownySettings.getMayorPostfix(resident);
        return "";
    }

    private String getNamePrefix(Resident resident) {

        if (resident == null)
            return "";
        if (resident.isKing())
            return TownySettings.getKingPrefix(resident);
        else if (resident.isMayor())
            return TownySettings.getMayorPrefix(resident);
        return "";
    }

    private boolean isMessageJson(String message) {
        try {
            JsonObject jsonObject = new Gson().fromJson(message, JsonObject.class);

            return jsonObject.has("text");
        } catch (JsonSyntaxException e) {
            return false;
        }
    }

    public static void usingPAPI(boolean using) {
        usingPAPI = using;
    }

    public static void usingTowny(boolean using) {
        usingTowny = using;
    }

    public static void usingBungee(boolean using) {
        usingBungee = using;
    }*/

    //taken from townychat source
    /**
     * Is this player Muted via Essentials?
     *
     * @param player
     * @return true if muted
     */
    /*private boolean isMuted(Player player) {
        // Check if essentials has this player muted.
        if (plugin.getTowny().isEssentials()) {
            try {
                if (plugin.getTowny().getEssentials().getUser(player).isMuted()) {
                    TownyMessaging.sendErrorMsg(player, Translation.of("tc_err_unable_to_talk_essentials_mute"));
                    return true;
                }
            } catch (TownyException e) {
                // Get essentials failed
            }
            return false;
        }
        return false;
    }*/
}
