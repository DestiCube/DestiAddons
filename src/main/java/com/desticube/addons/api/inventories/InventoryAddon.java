package com.desticube.addons.api.inventories;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class InventoryAddon {

    public final MiniMessage mm = MiniMessage.miniMessage();

    public String permission() {
        return null;
    }

    @NotNull
    public abstract String command();

    public abstract void openInventory(Player player);

}
