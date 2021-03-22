package com.paratopiamc.bungee_towny.command.chat;

import com.paratopiamc.bungee_towny.BungeeTowny;
import com.paratopiamc.bungee_towny.Translation;
import com.paratopiamc.bungee_towny.sql.SQLHost;
import com.paratopiamc.bungee_towny.sql.SQLMessage;
import com.paratopiamc.bungee_towny.synced.Players;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IgnoreCommandExecutor implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Translation.of("town.command.not_found"));
            return true;
        }

        Player player = ((Player) sender).getPlayer();
        String uuid = player.getUniqueId().toString();

        if (args.length == 0) {
            //tell the player who they ignore
            Bukkit.getScheduler().runTaskAsynchronously(BungeeTowny.getThisPlugin(), new Runnable() {
                @Override
                public void run() {
                    sender.sendMessage(Translation.of("chat.ignore.header"));
                    String ignoring = "";
                    for (String name : new Players().ignoringNames(uuid)) {
                        ignoring += "&6" + name + "&f, ";
                    }

                    if (ignoring.length() != 0) {
                        ignoring = ignoring.substring(0, ignoring.length() - 2);

                        sender.sendMessage(Translation.of("chat.ignore.info"));
                        sender.sendMessage(ignoring.replace("&", "\u00a7"));
                    } else {
                        sender.sendMessage(Translation.of("chat.ignore.info_no_ignoring"));
                    }

                    sender.sendMessage(Translation.of("chat.ignore.footer"));
                }
            });
            return true;
        } else if (args.length == 1) {
            Bukkit.getScheduler().runTaskAsynchronously(BungeeTowny.getThisPlugin(), new Runnable() {
                @Override
                public void run() {
                    String otherUuid = new Players().getUUID(args[0]);

                    SQLMessage message = new SQLMessage(SQLHost.getCredentials());

                    try {
                        ResultSet results = message.executeSelectSQL("SELECT ignored_by FROM players WHERE uuid = '" + otherUuid + "';");

                        if (results.next()) {
                            String ignore_list = results.getString("ignored_by");

                            if (ignore_list.contains(uuid)) {
                                ignore_list = ignore_list.replace("," + uuid, "");
                                sender.sendMessage(Translation.of("chat.ignore.no_longer").replace("{playername}", args[0]));
                            } else {
                                ignore_list += "," + uuid;
                                sender.sendMessage(Translation.of("chat.ignore.start").replace("{playername}", args[0]));
                            }

                            message.executeSQL(
                                    " UPDATE players " +
                                            "SET ignored_by = '" + ignore_list + "' " +
                                            "WHERE uuid ='" + otherUuid + "';"
                            );

                        } else {
                            sender.sendMessage(Translation.of("chat.ignore.not_found"));
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            return true;
        } else {
            sender.sendMessage(Translation.of("town.command.not_found"));
        }

        return true;
    }
}