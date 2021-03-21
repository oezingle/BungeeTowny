package com.paratopiamc.bungee_towny.command.chat.channel;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.ArrayList;
import java.util.List;

public class ChatAliasExecutor extends BukkitCommand {

    //private String command;
    private String channel;

    public ChatAliasExecutor(String command, String channel) {
        super(command);

        //this.command = command;
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

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return new ArrayList<>();
        //return super.tabComplete(sender, alias, args);
    }
}
