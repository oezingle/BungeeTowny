package com.paratopiamc.bungee_towny.command;

import com.paratopiamc.bungee_towny.BungeeTowny;
import com.paratopiamc.bungee_towny.Translation;
import com.paratopiamc.bungee_towny.chat.channel.Channels;
import com.paratopiamc.bungee_towny.listener.Listeners;
import com.paratopiamc.bungee_towny.sql.SQLHost;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AdminCommandExecutor implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
        if (args.length == 0) {
            Bukkit.getScheduler().runTaskAsynchronously(BungeeTowny.getThisPlugin(), () -> {
                sender.sendMessage(Translation.of("towny.command.admin.header").replace("{subcommand}", ""));
                if (sender.hasPermission("bungeetowny.admin")) {
                    sender.sendMessage(Translation.of("towny.command.admin.help.row1"));
                    sender.sendMessage(Translation.of("towny.command.admin.help.row2"));
                    sender.sendMessage(Translation.of("towny.command.admin.footer"));
                }
                sender.sendMessage(replaceColors("&7BungeeTowny by &6Oezingle&7, written for the &6ParatopiaMC &7server"));
                if (BungeeTowny.isSpigot()) {
                    //chatComponent

                    //bstats
                    TextComponent text = new TextComponent(replaceColors("&7bstats: "));
                    text.setUnderlined(true);
                    text.addExtra(replaceColors("&7bstats.org/plugin/bukkit/BungeeTowny"));

                    BaseComponent hover = new TextComponent("Visit this plugin's bstats page!");

                    text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://bstats.org/plugin/bukkit/BungeeTowny/10724"));
                    text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{hover}));

                    sender.spigot().sendMessage(text);

                    //spigot page?? idk
                }
                sender.sendMessage(Translation.of("towny.command.admin.footer"));
            });
            return true;
        } else {
            if (sender.hasPermission("bungeetowny.admin")) {

                switch (args[0]) {
                    case "reload":
                        if (sender.hasPermission("bungeetowny.admin.reload")) {
                            if (args.length == 1) {
                                BungeeTowny.reload();
                                sender.sendMessage(Translation.of("towny.command.admin.reload_success").replace("{module}", ""));
                                return true;
                            } else if (args.length == 2) {
                                //reload <module>
                                switch (args[1]) {
                                    case "chat":
                                        Channels.unRegisterChannels();
                                        BungeeTowny.reloadChat();
                                        sender.sendMessage(Translation.of("towny.command.admin.reload_success").replace("{module}", "chat"));
                                        return true;
                                    case "sql":
                                        BungeeTowny.reloadSQL();
                                        sender.sendMessage(Translation.of("towny.command.admin.reload_success").replace("{module}", "sql"));
                                        return true;
                                    case "messages":
                                        BungeeTowny.reloadMessages();
                                        sender.sendMessage(Translation.of("towny.command.admin.reload_success").replace("{module}", "messages"));
                                        return true;
                                    case "server":
                                        BungeeTowny.reloadServer();
                                        sender.sendMessage(Translation.of("towny.command.admin.reload_success").replace("{module}", "server"));
                                        return true;
                                    default:
                                        sender.sendMessage(Translation.of("towny.command.admin.unknown_arg"));
                                        return false;
                                }
                            } else {
                                sender.sendMessage(Translation.of("towny.command.admin.unknown_arg"));
                                break;
                            }
                        }
                    case "status":
                        if (sender.hasPermission("bungeetowny.admin.status")) {
                            boolean workingAsNormal = true;

                            sender.sendMessage(Translation.of("towny.command.admin.header").replace("{subcommand}", " status"));
                            if (Listeners.isUsingTowny()) {
                                sender.sendMessage(Translation.of("towny.command.admin.status.using_plugin").replace("{plugin}", "Towny"));
                            } else {
                                sender.sendMessage(Translation.of("towny.command.admin.status.not_using_plugin").replace("{plugin}", "Towny"));
                            }

                            if (Listeners.isUsingPAPI()) {
                                sender.sendMessage(Translation.of("towny.command.admin.status.using_plugin").replace("{plugin}", "PlaceholderAPI"));
                            } else {
                                sender.sendMessage(Translation.of("towny.command.admin.status.not_using_plugin").replace("{plugin}", "PlaceholderAPI"));
                            }

                            if (BungeeTowny.isSpigot()) {
                                sender.sendMessage(Translation.of("towny.command.admin.status.using_plugin").replace("{plugin}", "Spigot ChatComponent"));
                            } else {
                                sender.sendMessage(Translation.of("towny.command.admin.status.not_using_plugin").replace("{plugin}", "Spigot ChatComponent"));
                            }

                            if (Listeners.isUsingBungee()) {
                                sender.sendMessage(Translation.of("towny.command.admin.status.using_plugin").replace("{plugin}", "Bungeecord"));
                            } else {
                                sender.sendMessage(Translation.of("towny.command.admin.status.not_using_dependency").replace("{plugin}", "Bungeecord"));
                                workingAsNormal = false;
                            }

                            if (SQLHost.getMessenger().isConnected()) {
                                sender.sendMessage(Translation.of("towny.command.admin.status.using_plugin").replace("{plugin}", "SQL"));
                            } else {
                                sender.sendMessage(Translation.of("towny.command.admin.status.not_using_dependency").replace("{plugin}", "SQL"));
                                workingAsNormal = false;
                            }

                            if (!workingAsNormal) {
                                sender.sendMessage(Translation.of("towny.command.admin.status.not_working"));
                            }

                            sender.sendMessage(Translation.of("towny.command.admin.footer"));
                            if (Listeners.isUsingChat()) {
                                sender.sendMessage(Translation.of("towny.command.admin.status.module_enabled").replace("{module}", "Chat"));
                            } else {
                                sender.sendMessage(Translation.of("towny.command.admin.status.module_disabled").replace("{module}", "Chat"));
                            }
                            sender.sendMessage(Translation.of("towny.command.admin.footer"));

                            return true;
                        }
                    default:
                        sender.sendMessage(Translation.of("towny.command.admin.unknown_arg"));
                        return true;
                }
            } else {
                return false;
            }
        }

        return false;
    }

    String replaceColors(String in) {
        return in.replace("&", "\u00a7");
    }
}