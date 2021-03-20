package com.paratopiamc.bungee_towny.chat.chatcommand;

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

    public boolean execute(CommandSender commandSender, String label, String[] args) {

        return true;
    }
}
