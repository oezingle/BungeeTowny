package com.paratopiamc.bungee_towny.command.chat.msg;

import com.paratopiamc.bungee_towny.BungeeTowny;
import com.paratopiamc.bungee_towny.Translation;
import com.paratopiamc.bungee_towny.chat.Channels;
import com.paratopiamc.bungee_towny.chat.ChatSendEvent;
import com.paratopiamc.bungee_towny.synced.Players;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MsgCommandExecutor implements CommandExecutor {

    boolean togglable = false;
    int timeout = 0;

    public MsgCommandExecutor(ConfigurationSection config) {
        togglable = config.getBoolean("toggle");

        timeout = config.getInt("timeout");
    }

    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
        if (args.length == 0) {
            return false;
        } else if (args.length == 1 && togglable) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Translation.of("chat.msg.cant_message"));
                return true;
            }

            String uuid = ((Player) sender).getPlayer().getUniqueId().toString();

            String playerName = args[0];
            //set the channel without a message
            if (timeout > 0) {
                sender.sendMessage(
                        Translation.of("chat.msg.convo_set")
                                .replace("{playername}", playerName)
                                .replace("{time}",Integer.toString(timeout))
                );

                String lastChannel = Players.getChannel(uuid);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        sender.sendMessage(Translation.of("chat.msg.convo_expired"));

                        Players.setChannel(lastChannel, uuid);
                    }
                }.runTaskLater(BungeeTowny.getThisPlugin(), 20 * 60 * timeout); //20 * 60 * timeout
            } else {
                sender.sendMessage(Translation.of("chat.msg.convo_set_no_expire").replace("{playername}", playerName));
            }
            //switch the channel
            Players.setChannel("msg|" + playerName, uuid);
        } else if (args.length > 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Translation.of("chat.msg.cant_message"));
                return true;
            }

            //msg <player> <message>
            String message = "";
            for (short i = 1; i < args.length; i++) {
                message += args[i] + " ";
            }

            String uuid = ((Player) sender).getPlayer().getUniqueId().toString();

            String playerName = args[0];

            String lastChannel = Players.getChannel(uuid);

            Players.setChannel("msg|" + playerName, uuid);
            ChatSendEvent event = new ChatSendEvent(message, Channels.get("msg"), ((Player) sender).getPlayer(), !Bukkit.isPrimaryThread());
            Bukkit.getPluginManager().callEvent(event);
            Players.setChannel(lastChannel, uuid);

        }

        return true;
    }
}
