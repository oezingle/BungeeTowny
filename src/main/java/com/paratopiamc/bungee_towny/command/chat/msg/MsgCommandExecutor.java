package com.paratopiamc.bungee_towny.command.chat.msg;

import com.paratopiamc.bungee_towny.BungeeTowny;
import com.paratopiamc.bungee_towny.Translation;
import com.paratopiamc.bungee_towny.chat.Channels;
import com.paratopiamc.bungee_towny.chat.ChatSendEvent;
import com.paratopiamc.bungee_towny.synced.players.Players;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MsgCommandExecutor implements CommandExecutor {

    boolean togglable = false;
    double timeout = 0;

    public MsgCommandExecutor(ConfigurationSection config) {
        togglable = config.getBoolean("toggle");

        timeout = config.getDouble("timeout");
    }

    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
        if (args.length == 0) {
            return false;
        } else if (args.length == 1) {
            if (togglable) {
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
                                    .replace("{time}", Double.toString(timeout))
                    );

                    String lastChannel = new Players().getChannel(uuid);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            sender.sendMessage(Translation.of("chat.msg.convo_expired"));


                            //TODO Is this preferred over quietly setting the channel?
                            Bukkit.dispatchCommand(sender, "chat " + lastChannel);
                            //Players.setChannel(lastChannel, uuid);
                        }
                    }.runTaskLater(BungeeTowny.getThisPlugin(), (int) (20 * 60 * timeout)); //20 * 60 * timeout
                } else {
                    sender.sendMessage(Translation.of("chat.msg.convo_set_no_expire").replace("{playername}", playerName));
                }
                //switch the channel
                new Players().setChannel("msg." + playerName + ".none", uuid);
            } else {
                sender.sendMessage(Translation.of("towny.command.not_found"));
                return true;
            }
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

            Players players = new Players();

            String lastChannel = players.getChannel(uuid);
            if (lastChannel.contains("msg.")) {
                //grab this channel again
                lastChannel = lastChannel.split(".")[3];
            }

            String playerName = args[0];

            //                             using msg
            //                                      recipient          channel to return to
            players.setChannel("msg." + playerName + "." + lastChannel, uuid);
            ChatSendEvent event = new ChatSendEvent(message, Channels.get("msg"), ((Player) sender).getPlayer(), !Bukkit.isPrimaryThread());
            Bukkit.getPluginManager().callEvent(event);

        }

        return true;
    }
}
