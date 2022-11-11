package com.desticube.addons.api.customtext;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class CustomTextAddon {

    public final MiniMessage mm = MiniMessage.miniMessage();

    @NotNull
    public abstract String command();

    public String permission() {
        return null;
    }

    public abstract Component getMessage();

    public void sendMessage(CommandSender sender) {
        sender.sendMessage(getMessage());
    }

}
