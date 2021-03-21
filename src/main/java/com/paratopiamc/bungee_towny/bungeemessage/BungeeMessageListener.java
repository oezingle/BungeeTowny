package com.paratopiamc.bungee_towny.bungeemessage;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.paratopiamc.bungee_towny.BungeeTowny;
import com.paratopiamc.bungee_towny.listener.Listeners;
import com.paratopiamc.bungee_towny.synced.Bungeecord;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


public class BungeeMessageListener implements PluginMessageListener {

    Plugin plugin;

    public BungeeMessageListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player notUsedPlayer, byte[] byteMessage) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(byteMessage);
        String subchannel = in.readUTF();

        switch (subchannel) {
            case "GetServer":
                BungeeTowny.waitForBungeePlayer.cancel();

                String serverName = in.readUTF();

                if (serverName != null) {
                    BungeeTowny.setServerName(serverName);
                }
                Listeners.usingBungee(true);

                break;
            case "PlayerList":

                String server = in.readUTF(); // The name of the server you got the player list of

                if (server.equals("ALL")) {
                    String input = in.readUTF();

                    String[] players = input.split(", ");
                }

                break;

            case "BungeeTowny":
                short len = in.readShort();
                byte[] msgbytes = new byte[len];
                in.readFully(msgbytes);

                DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));

                //Lazy exception handler
                String outdata = "no_data";
                try {
                    outdata = msgin.readUTF(); // Read the data in the same way you wrote it
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //outdata should be a json value

                JsonObject jsonObject = new Gson().fromJson(outdata, JsonObject.class);

                String command = jsonObject.get("command").getAsString();

                switch (command) {
                    case "permission_chat": {
                        String permission = jsonObject.get("permission").getAsString();
                        String message = jsonObject.get("message").getAsString();

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.hasPermission(permission)) {
                                //TODO spigot chatAPI
                                player.sendMessage(message);
                            }
                        }

                        break;
                    }
                    case "message": {
                        String recipient = jsonObject.get("recipient").getAsString();
                        String message = jsonObject.get("message").getAsString();

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.getName().equalsIgnoreCase(recipient)) {
                                player.sendMessage(message);
                            }
                        }

                        break;
                    }

                    case "check_queue": {
                        //TODO check mysql queue for actions
                        break;
                    }

                    default:
                        BungeeTowny.getThisPlugin().getLogger().log(Level.WARNING, "Unknown BungeeTowny messaging channel command \"" + command + "\" received");
                        break;
                }

        }
    }
}
