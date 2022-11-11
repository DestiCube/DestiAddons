package com.desticube.addons.api.listeners;

import com.desticube.addons.AddonsMain;
import com.desticube.addons.api.AddonManager;
import com.desticube.addons.api.inventories.InventoryAddon;
import com.gamerduck.commons.commands.AbstractDuckCommand;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static java.io.File.separator;

public class Listeners implements AddonManager {

    public final ArrayList<ListenerAddon> expansions = Lists.newArrayList();

    public void register(AddonsMain main) {
        expansions.stream().peek(ex -> ex.register(main));
    }

    public CompletableFuture<AddonsMain> loadFiles(AddonsMain main) {
        return CompletableFuture.supplyAsync(() -> {
            File folder = new File(main.getDataFolder() + separator + "addons" + separator + "listeners");
            if (!folder.exists()) folder.mkdirs();
            if (folder.isDirectory()) {
                for (File file : folder.listFiles()) {
                    final Class<? extends ListenerAddon> expansionClass = main.findClass(file, ListenerAddon.class);

                    if (expansionClass == null) {
                        Bukkit.getLogger().severe("Failed to load expansion" + file.getName() + ", as it does not " +
                                "have a class which extends ListenerAddon");
                        continue;
                    }
                    try {
                        expansions.add(expansionClass.getDeclaredConstructor().newInstance());
                    } catch (NoSuchMethodException | InstantiationException |
                             IllegalAccessException | InvocationTargetException ex) {
                            ex.printStackTrace();
                        }
                    }

                }
            return main;
        });
    }
}
