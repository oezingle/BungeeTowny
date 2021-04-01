package com.paratopiamc.bungee_towny.chat;

import com.paratopiamc.bungee_towny.BungeeTowny;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ChatComponentWrapper {

    TextComponent textComponent;

    public ChatComponentWrapper() {
        textComponent = new TextComponent();
    }

    public void setUnderlined(boolean underlined) {
        textComponent.setUnderlined(underlined);
    }

    public void addExtra(String text) {
        textComponent.addExtra(text);
    }

    public void setClickEvent(String type, String value) {
        ClickEvent.Action action = ClickEvent.Action.valueOf(type.toUpperCase());

        textComponent.setClickEvent(new ClickEvent(action, value));
    }

    public void setHoverText(String message) {
        BaseComponent hover = new TextComponent(message);

        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{hover}));
    }

    public void fromJson(String json, List<CommandSender> recipients) {
        Bukkit.getScheduler().runTaskAsynchronously(BungeeTowny.getThisPlugin(), () -> {

            for (CommandSender recipient : recipients) {
                fromJson(json, recipient);
            }
        });
    }

    public void fromJson(String json, CommandSender recipient) {
        BaseComponent baseComponent[] = ComponentSerializer.parse(json);

        recipient.spigot().sendMessage(baseComponent);
    }

    public void send(CommandSender recipient) {
        recipient.spigot().sendMessage(textComponent);
    }

    public void send(List<CommandSender> recipients) {
        Bukkit.getScheduler().runTaskAsynchronously(BungeeTowny.getThisPlugin(), () -> {
            for (CommandSender recipient : recipients) {
                send(recipient);
            }

        });
    }

    public void admin(CommandSender sender) {
        //chatComponent

        //bstats
        TextComponent text = new TextComponent(replaceColors("&6bstats: "));
        text.setUnderlined(true);
        text.addExtra(replaceColors("&7Bstats.org/plugin/bukkit/BungeeTowny"));

        BaseComponent hover = new TextComponent("Visit this plugin's bstats page!");

        text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://bstats.org/plugin/bukkit/BungeeTowny/10724"));
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{hover}));

        sender.spigot().sendMessage(text);

        //spigot page?? idk
    }

    private String replaceColors(String in) {
        return in.replace("&", "\u00a7");
    }
}
