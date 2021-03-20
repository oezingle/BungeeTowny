package com.paratopiamc.bungee_towny.chat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Resident;
import com.paratopiamc.bungee_towny.BungeeTowny;
import com.paratopiamc.bungee_towny.Translation;
import com.paratopiamc.bungee_towny.bungeemessage.BungeeMessage;
import com.paratopiamc.bungee_towny.chat.Channel.Channel;
import com.paratopiamc.bungee_towny.listener.Listeners;
import com.paratopiamc.bungee_towny.synced.Nations;
import com.paratopiamc.bungee_towny.synced.Players;
import com.paratopiamc.bungee_towny.synced.Towns;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class ChatSendEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public ChatSendEvent(String playerMessage, Channel channel, Player player, boolean async) {
        super(async);

        String format = channel.getFormat();

        if (format != null) {
            String uuid = player.getUniqueId().toString();

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

            String town = Players.getTown(uuid);
            String nation = Players.getNation(uuid);

            String townformatted = town == "townless" ? "" : toUpperStart(town);
            String nationfomatted = nation == "nationless" ? "" : toUpperStart(nation);
            String bothformatted = ChatFormats.getBoth()
                    .replace("%t", toUpperStart(town))
                    .replace("%n", toUpperStart(nation));
            bothformatted = String.format(bothformatted, town, nation);

            //set the message from the format
            String chatMessage = format
                    .replace("{playername}", player.getName())
                    .replace("{modplayername}", player.getDisplayName())
                    .replace("{worldname}", ChatFormats.getWorld().replace("%s", player.getWorld().getName()))
                    .replace("{town}", town)
                    .replace("{towntagoverride}", ChatFormats.toTown(townformatted))
                    .replace("{towntag}", ChatFormats.toTown(townformatted))
                    .replace("{townformatted}", townformatted)
                    .replace("{nation}", nation)
                    .replace("{nationformatted}", nationfomatted)
                    .replace("{nationtag}", ChatFormats.toNation(nationfomatted))
                    .replace("{nationtagoverride}", ChatFormats.toNation(nationfomatted))
                    .replace("{townytag}", bothformatted)
                    .replace("{townytagoverride}", bothformatted)
                    .replace("{townyformatted}", bothformatted)
                    .replace("{servername}", ChatFormats.getWorld().replace("%s", BungeeTowny.getServerName()));

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
                        .replace("{townycolor}", resident.isMayor() ? (resident.isKing() ? ChatColors.getKingColor() : ChatColors.getMayorColor()) : ChatColors.getResidentColor())
                        .replace("{townyprefix}", resident.hasTitle() ? " " + Players.getTitle(uuid) : getNamePrefix(resident))
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

                chatMessage = chatMessage.replace("{title}", Players.getTitle(uuid));
            }


            //do the PAPI
            if (Listeners.isUsingPAPI()) {
                chatMessage = PlaceholderAPI.setPlaceholders(player, chatMessage);
            }

            chatMessage = replaceColors(chatMessage);

            //do the message last so everything else can be color coded
            chatMessage = chatMessage.replace("{msg}", playerMessage);

            //TODO consider range
            //TODO consider spying
            //TODO essentials mute, essentials ignore

            if (Listeners.isUsingBungee()) {

                //use the mysql db to figure out recipients
                Collection<String> recipients = new ArrayList<>();

                switch (channel.getType()) {
                    case TOWN:
                        if (channel.isJSON()) {
                            jsonSend(Towns.getResidentNames(town), chatMessage);
                        } else {
                            simpleSend(Towns.getResidentNames(town), chatMessage);
                        }
                        break;
                    case NATION:
                        if (channel.isJSON()) {
                            jsonSend(Nations.getResidentNames(nation), chatMessage);
                        } else {
                            simpleSend(Nations.getResidentNames(nation), chatMessage);
                        }
                        break;
                    case GLOBAL:
                        if (channel.isJSON()) {
                            jsonSend(Towns.getAllNames(), chatMessage);
                        } else {
                            simpleSend(Towns.getAllNames(), chatMessage);
                        }
                        break;
                    default:
                        //send a plugin message to the other servers, because we have to check permissions with these
                        Plugin plugin = Listeners.getPlugin();
                        BungeeMessage messages = new BungeeMessage(plugin);

                        for (String recipient : recipients) {
                            messages.writeStrings(new String[]{"", recipient, chatMessage});

                            messages.send();
                        }

                        break;
                }

            } else {
                String bad_config = replaceColors(Translation.of("chat.bad_config"));
                player.sendMessage(bad_config);
                Bukkit.getPluginManager().getPlugin("BungeeTowny").getLogger().info(bad_config);
            }
        } else {
            player.sendMessage(replaceColors(Translation.of("chat.null_channel")));
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

    public void simpleSend(List<String> recipients, String message) {
        Plugin plugin = Listeners.getPlugin();
        BungeeMessage messages = new BungeeMessage(plugin);

        for (String recipient : recipients) {
            messages.writeStrings(new String[]{"Message", recipient, message});

            messages.send();
        }
    }

    public void jsonSend(List<String> recipients, String message) {
        Plugin plugin = Listeners.getPlugin();
        BungeeMessage messages = new BungeeMessage(plugin);

        for (String recipient : recipients) {
            messages.writeStrings(new String[]{"MessageRaw", recipient, message});

            messages.send();
        }
    }
}
