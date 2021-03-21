package com.paratopiamc.bungee_towny.chat.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

public class ChatAliasExecutor extends BukkitCommand {

    private String command;
    private String channel;

    public ChatAliasExecutor(String command, String channel) {
        super(command);

        this.command = command;
        this.channel = channel;
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        String argList = "";
        for (String arg : args) {
            argList += arg + " ";
        }

        Bukkit.dispatchCommand(sender, "chat " + channel + " " + argList);

        return true;
    }
}
