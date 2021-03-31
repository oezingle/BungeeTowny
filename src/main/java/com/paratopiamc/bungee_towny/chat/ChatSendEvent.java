package com.paratopiamc.bungee_towny.chat;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Resident;
import com.paratopiamc.bungee_towny.BungeeTowny;
import com.paratopiamc.bungee_towny.Translation;
import com.paratopiamc.bungee_towny.bungeemessage.BungeeMessage;
import com.paratopiamc.bungee_towny.chat.channel.Channel;
import com.paratopiamc.bungee_towny.listener.Listeners;
import com.paratopiamc.bungee_towny.synced.Nations;
import com.paratopiamc.bungee_towny.synced.Players;
import com.paratopiamc.bungee_towny.synced.Towns;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class ChatSendEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public ChatSendEvent(String playerMessage, Channel channel, Player player, boolean async) {
        super(async);

        Players players = new Players();

        String uuid = player.getUniqueId().toString();

        if (channel == null) {
            Bukkit.dispatchCommand(player, "chat general");
        }

        String format = channel.getFormat();

        //TODO sync with essentials
        if (players.isMuted(uuid)) {
            player.sendMessage(Translation.of("chat.muted"));
            return;
        }

        if (format != null) {
            //taken from townychat
            if (playerMessage.contains("&L") || playerMessage.contains("&l") ||
                    playerMessage.contains("&O") || playerMessage.contains("&o") ||
                    playerMessage.contains("&N") || playerMessage.contains("&n") ||
                    playerMessage.contains("&K") || playerMessage.contains("&k") ||
                    playerMessage.contains("&M") || playerMessage.contains("&m") ||
                    playerMessage.contains("&R") || playerMessage.contains("&r")) {
                if (!player.hasPermission("townychat.chat.format.bold")) {
                    playerMessage = playerMessage.replaceAll("&L", "");
                    playerMessage = playerMessage.replaceAll("&l", "");
                }
                if (!player.hasPermission("townychat.chat.format.italic")) {
                    playerMessage = playerMessage.replaceAll("&O", "");
                    playerMessage = playerMessage.replaceAll("&o", "");
                }
                if (!player.hasPermission("townychat.chat.format.magic")) {
                    playerMessage = playerMessage.replaceAll("&K", "");
                    playerMessage = playerMessage.replaceAll("&k", "");
                }
                if (!player.hasPermission("townychat.chat.format.underlined")) {
                    playerMessage = playerMessage.replaceAll("&N", "");
                    playerMessage = playerMessage.replaceAll("&n", "");
                }
                if (!player.hasPermission("townychat.chat.format.strike")) {
                    playerMessage = playerMessage.replaceAll("&M", "");
                    playerMessage = playerMessage.replaceAll("&m", "");
                }
                if (!player.hasPermission("townychat.chat.format.reset")) {
                    playerMessage = playerMessage.replaceAll("&R", "");
                    playerMessage = playerMessage.replaceAll("&r", "");
                }
            }


            //message = replaceColors(message));

            if (player.hasPermission("townychat.chat.color")) {
                playerMessage = ChatColor.translateAlternateColorCodes('&', playerMessage);

                if (Listeners.isUsingTowny()) {
                    if (Towny.is116Plus()) {
                        playerMessage = replaceColors(playerMessage);
                    }
                }
            }

            String town = players.getTown(uuid);
            String nation = players.getNation(uuid);

            //TODO replace this whole section with something better.
            String townformatted = town == "townless" ? "" : toUpperStart(town);
            String nationfomatted = nation == "nationless" ? "" : toUpperStart(nation);
            String bothformatted = Translation.of("chat.towny.formats.both")
                    .replace("%t", toUpperStart(town))
                    .replace("%n", toUpperStart(nation));
            bothformatted = String.format(bothformatted, town, nation);

            //set the message from the format
            String chatMessage = format
                    .replace("{modplayername}", player.getDisplayName())
                    .replace("{worldname}", Translation.of("chat.towny.formats.world").replace("%s", player.getWorld().getName()))
                    .replace("{town}", town)
                    .replace("{towntagoverride}", Translation.of("chat.towny.formats.town").replace("%s",townformatted))
                    .replace("{towntag}", Translation.of("chat.towny.formats.town").replace("%s",townformatted))
                    .replace("{townformatted}", townformatted)
                    .replace("{nation}", nation)
                    .replace("{nationformatted}", nationfomatted)
                    .replace("{nationtag}", Translation.of("chat.towny.formats.nation").replace("%s",nationfomatted))
                    .replace("{nationtagoverride}", Translation.of("chat.towny.formats.nation").replace("%s",nationfomatted))
                    .replace("{townytag}", bothformatted)
                    .replace("{townytagoverride}", bothformatted)
                    .replace("{townyformatted}", bothformatted)
                    .replace("{servername}", Translation.of("chat.towny.formats.world").replace("%s", BungeeTowny.getServerName()));

            switch (channel.getType()) {
                case MESSAGE:
                    break;
                default:
                    chatMessage = chatMessage.replace("{playername}", player.getName());
                    break;
            }

            //replace all the towny tags, but if we can't find them just strip em out
            if (Listeners.isUsingTowny()) {
                Resident resident = TownyUniverse.getInstance().getResident(player.getUniqueId());

                chatMessage = chatMessage
                        .replace("{title}", resident.getTitle())
                        .replace("{permprefix}", TownyUniverse.getInstance().getPermissionSource().getPrefixSuffix(resident, "prefix"))
                        .replace("{permsuffix}", TownyUniverse.getInstance().getPermissionSource().getPrefixSuffix(resident, "suffix"))
                        .replace("{permuserprefix}", TownyUniverse.getInstance().getPermissionSource().getPrefixSuffix(resident, "userprefix"))
                        .replace("{permusersuffix}", TownyUniverse.getInstance().getPermissionSource().getPrefixSuffix(resident, "usersuffix"))
                        .replace("{permgroupprefix}", TownyUniverse.getInstance().getPermissionSource().getPrefixSuffix(resident, "groupprefix"))
                        .replace("{permgroupsuffix}", TownyUniverse.getInstance().getPermissionSource().getPrefixSuffix(resident, "groupsuffix"))
                        .replace("{group}", TownyUniverse.getInstance().getPermissionSource().getPlayerGroup(player))
                        .replace("{townycolor}", resident.isMayor() ? (resident.isKing() ? Translation.of("chat.towny.colors.king") : Translation.of("chat.towny.colors.mayor")) : Translation.of("chat.towny.colors.resident"))
                        .replace("{townyprefix}", resident.hasTitle() ? " " + players.getTitle(uuid) : getNamePrefix(resident))
                        .replace("{townypostfix}", resident.hasSurname() ? " " + resident.getSurname() : getNamePostfix(resident))
                        .replace("{townynameprefix}", getNamePrefix(resident))
                        .replace("{townynamepostfix}", getNamePostfix(resident))
                        .replace("{surname}", resident.hasSurname() ? " " + resident.getSurname() : "");
            } else {
                String[] unusable_tags = {
                        "{permprefix}", "{permsuffix}", "{group}", "{permuserprefix}", "{permusersuffix}", "{permgroupprefix}", "{permgroupsuffix}",
                        "{townycolor}", "{townyprefix}", "{townypostfix}", "{townynameprefix}", "{townynamepostfix}", "{surname}"};

                for (String tag : unusable_tags) {
                    chatMessage = chatMessage.replace(tag, "");
                }

                chatMessage = chatMessage.replace("{title}", players.getTitle(uuid));
            }


            //do the PAPI
            if (Listeners.isUsingPAPI()) {
                chatMessage = PlaceholderAPI.setPlaceholders(player, chatMessage);
            }

            chatMessage = replaceColors(chatMessage);

            //do the message last so everything else can be color coded
            chatMessage = chatMessage
                    .replace("{msg}", playerMessage)
                    .replace("  ", " ");

            String finalMessage = chatMessage;

            Bukkit.getScheduler().runTaskAsynchronously(BungeeTowny.getThisPlugin(), new Runnable() {
                @Override
                public void run() {

                    String[] ignores = new Players().ignoredByNames(uuid);

                    //TODO consider range
                    //TODO consider spying
                    //TODO essentials mute, essentials ignore

                    if (Listeners.isUsingBungee()) {
                        //use the mysql db to figure out recipients
                        switch (channel.getType()) {
                            case TOWN:
                                if (channel.isJSON()) {
                                    jsonSend(new Towns().getResidentNames(town), finalMessage, ignores);
                                } else {
                                    simpleSend(new Towns().getResidentNames(town), finalMessage, ignores);
                                }
                                break;
                            case NATION:
                                if (channel.isJSON()) {
                                    jsonSend(new Nations().getResidentNames(nation), finalMessage, ignores);
                                } else {
                                    simpleSend(new Nations().getResidentNames(nation), finalMessage, ignores);
                                }
                                break;
                            case GLOBAL:
                                if (channel.isJSON()) {
                                    jsonSend(new Towns().getAllNames(), finalMessage, ignores);
                                } else {
                                    simpleSend(new Towns().getAllNames(), finalMessage, ignores);
                                }
                                break;
                            case MESSAGE: {

                                String[] messageInfo = players.getChannel(uuid).split("@");
                                String sendTo = messageInfo[1];
                                String returnChannel = messageInfo[2];

                                Player sendToPlayer = null;
                                for (Player candidate : Bukkit.getOnlinePlayers()) {
                                    if (candidate.getName().equalsIgnoreCase(sendTo))
                                        sendToPlayer = candidate;
                                }

                                for (String ignore : ignores) {
                                    if (ignore.equalsIgnoreCase(sendTo)) {
                                        player.sendMessage(Translation.of("chat.msg.cant_message"));
                                        return;
                                    }
                                }

                                player.sendMessage(finalMessage
                                        .replace("{fromto}", Translation.of("chat.msg.to"))
                                        .replace("{playername}", sendTo));

                                String fromMessage = finalMessage
                                        .replace("{fromto}", Translation.of("chat.msg.from"))
                                        .replace("{playername}", player.getName());

                                if (sendToPlayer != null) {
                                    sendToPlayer.sendMessage(fromMessage);
                                } else {
                                    Plugin plugin = BungeeTowny.getThisPlugin();

                                    BungeeMessage messages = new BungeeMessage(plugin);

                                    messages.sendPluginMessage(
                                            "{" +
                                                    "   \"command\":\"message\"," +
                                                    "   \"recipient\":\"" + sendTo + "\"," +
                                                    "   \"message\":\"" + fromMessage + "\"" +
                                                    "}");
                                }

                                //finally, return the sender to their channel
                                if (!returnChannel.equalsIgnoreCase("none")) {
                                    players.setChannel(returnChannel, uuid);
                                }

                                break;
                            }
                            default: {
                                //send a plugin message to the other servers, because we have to check permissions with these
                                Plugin plugin = BungeeTowny.getThisPlugin();

                                String permission = channel.getPermission();

                                BungeeMessage messages = new BungeeMessage(plugin);
                                messages.sendPluginMessage(
                                        "{" +
                                                "   \"command\":\"permission_chat\"," +
                                                "   \"permission\":\"" + permission + "\"," +
                                                "   \"message\":\"" + finalMessage + "\"," +
                                                "   \"from_uuid\":\"" + uuid + "\"" +
                                                "}");

                                //do it locally
                                for (Player candidate : Bukkit.getOnlinePlayers()) {
                                    if (candidate.hasPermission(permission)) {
                                        //check ignore list
                                        boolean shouldSend = true;
                                        for (String ignore : ignores) {
                                            if (player.getName().equalsIgnoreCase(ignore)) {
                                                shouldSend = false;
                                            }
                                        }

                                        if (shouldSend) {
                                            //TODO spigot chatAPI
                                            candidate.sendMessage(finalMessage);
                                        }
                                    }
                                }

                                break;
                            }
                        }
                    } else {
                        String bad_config = Translation.of("chat.bad_config");
                        player.sendMessage(bad_config);
                        Bukkit.getPluginManager().getPlugin("BungeeTowny").getLogger().info(bad_config + " | You need bungeecord for this plugin to work");
                    }
                }
            });
        } else {
            player.sendMessage(Translation.of("chat.null_channel"));
        }
    }

    private String replaceColors(String message) {
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

    private String toUpperStart(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public void simpleSend(List<String> recipients, String message, String[] ignores) {

        for (String ingore : ignores) {
            recipients.remove(ingore);
        }

        Plugin plugin = BungeeTowny.getThisPlugin();
        BungeeMessage messages = new BungeeMessage(plugin);

        for (String recipient : recipients) {

            messages.writeStrings(new String[]{"Message", recipient, message});

            messages.send();
        }
    }

    public void jsonSend(List<String> recipients, String message, String[] ignores) {

        for (String ingore : ignores) {
            recipients.remove(ingore);
        }

        Plugin plugin = BungeeTowny.getThisPlugin();
        BungeeMessage messages = new BungeeMessage(plugin);

        for (String recipient : recipients) {
            messages.writeStrings(new String[]{"MessageRaw", recipient, message});

            messages.send();
        }
    }
}
