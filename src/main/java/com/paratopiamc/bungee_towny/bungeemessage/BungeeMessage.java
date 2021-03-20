package com.paratopiamc.bungee_towny.bungeemessage;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BungeeMessage {

    Plugin plugin;
    public ByteArrayDataOutput bytesOut;

    public BungeeMessage(Plugin plugin) {
        this.plugin = plugin;
        bytesOut = ByteStreams.newDataOutput();
    }

    public boolean send() {
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            //the event doesn't get a return but this is the most I'll do without callbacks
            return false;
        }
        Bukkit.getOnlinePlayers().iterator().next().sendPluginMessage(plugin, "BungeeCord", bytesOut.toByteArray());

        //Allows recycling
        bytesOut = ByteStreams.newDataOutput();

        return true;
    }

    public void writeString(String in) {
        bytesOut.writeUTF(in);
    }

    public void writeStrings(String[] ins) {
        for (String in : ins) {
            bytesOut.writeUTF(in);
        }
    }

    public void sendPluginMessage(String message) {
        System.out.println(message);

        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);
        try {
            msgout.writeUTF(message); // You can do anything you want with msgout
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        writeStrings(new String[]{"Forward", "ALL", "BungeeTowny"});
        bytesOut.writeShort(msgbytes.toByteArray().length);
        bytesOut.write(msgbytes.toByteArray());

        send();
    }

}