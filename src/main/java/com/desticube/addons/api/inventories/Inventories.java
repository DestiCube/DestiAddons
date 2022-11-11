package com.desticube.addons.api.inventories;

import com.desticube.addons.AddonsMain;
import com.desticube.addons.api.AddonManager;
import com.gamerduck.commons.commands.AbstractDuckCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import static java.io.File.separator;

public class Inventories implements AddonManager {

    public final ConcurrentHashMap<String, InventoryAddon> expansions = new ConcurrentHashMap<>();

    public void register(AddonsMain main) {
        expansions.keySet().forEach(c -> {
            AbstractDuckCommand command = new AbstractDuckCommand() {
                @Override
                public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
                    InventoryAddon addon = expansions.get(c);
                    if (sender instanceof Player p) {
                        if (addon.permission() == null || addon.permission().equalsIgnoreCase("")
                                || sender.hasPermission(addon.permission())) {
                            expansions.get(c).openInventory(p);
                        }
                    }
                    return false;
                }
            };
            command.register(c, "/" + c, "Custom Inventory", "", null, "");
        });
    }

    public void loadFiles(AddonsMain main) {
            File folder = new File(main.getDataFolder() + separator + "addons" + separator + "inventories");
            if (!folder.exists()) folder.mkdirs();
            if (folder.isDirectory()) {
                for (File file : folder.listFiles()) {
                    final Class<? extends InventoryAddon> expansionClass = main.findClass(file, InventoryAddon.class);

                    if (expansionClass == null) {
                        Bukkit.getLogger().severe("Failed to load expansion" + file.getName() + ", as it does not " +
                                "have a class which extends InventoryAddon");
                        continue;
                    }
                    try {
                        InventoryAddon expansion = expansionClass.getDeclaredConstructor().newInstance();
                        expansions.put(expansion.command(), expansion);
                    } catch (NoSuchMethodException | InstantiationException |
                            IllegalAccessException | InvocationTargetException ex) {
                        ex.printStackTrace();
                    }

                }
            }
    }
}
