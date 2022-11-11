package com.desticube.addons.api.listeners;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ListenerAddon implements Listener {

    final MiniMessage mm = MiniMessage.miniMessage();

    public void register(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

}
