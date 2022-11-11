package com.desticube.addons.api.customtext;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class CustomTextAddon {

    @NotNull
    public abstract String command();

    public abstract Component getMessage();

    public void sendMessage(CommandSender sender) {
        sender.sendMessage(getMessage());
    }

}
