package com.paratopiamc.bungee_towny.command.chat.msg;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.List;

public class MsgCommandAlias extends BukkitCommand {

    public MsgCommandAlias(String command) {
        super(command);
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        String argList = "";
        for (String arg : args) {
            argList += arg + " ";
        }

        Bukkit.dispatchCommand(sender, "message " + argList);

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return new MsgCommandTabCompletor().onTabComplete(sender, null, alias, args);
        //return super.tabComplete(sender, alias, args);
    }
}
