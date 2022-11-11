package com.desticube.addons.api.inventories;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class InventoryAddon {

    @NotNull
    public abstract String command();

    public abstract void openInventory(Player player);

}
