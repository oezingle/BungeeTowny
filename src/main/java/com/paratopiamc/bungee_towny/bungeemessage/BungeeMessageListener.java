package com.paratopiamc.bungee_towny.bungeemessage;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paratopiamc.bungee_towny.BungeeTowny;
import com.paratopiamc.bungee_towny.listener.Listeners;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;


public class BungeeMessageListener implements PluginMessageListener {

    Plugin plugin;

    public BungeeMessageListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player notUsedPlayer, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        switch (subchannel) {
            case "GetServer":
                BungeeTowny.waitForBungeePlayer.cancel();
                BungeeTowny.setServerName(in.readUTF());

                Listeners.usingBungee(true);

                break;

            case "PlayerList":

                String server = in.readUTF(); // The name of the server you got the player list of

                if (server.equals("ALL")) {
                    String[] playerList = in.readUTF().split(", ");

                    for (String player : playerList) {
                        System.out.println(player);
                    }
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

                String command = jsonObject.get("command").toString();

                System.out.println("command: " + command);

                switch (command) {
                    case "check-queue":
                        //TODO check mysql queue for actions
                        break;
                    case "permission-chat":
                        //TODO send the message to the players who have the permission
                }

        }
    }
}
