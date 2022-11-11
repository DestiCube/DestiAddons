package com.desticube.addons.api.customtext;

import com.desticube.addons.AddonsMain;
import com.desticube.addons.api.AddonManager;
import com.gamerduck.commons.commands.AbstractDuckCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static java.io.File.separator;

public class CustomTexts implements AddonManager {

    public final ConcurrentHashMap<String, CustomTextAddon> expansions = new ConcurrentHashMap<>();

    public void register(AddonsMain main) {
        expansions.keySet().forEach(c -> {
            AbstractDuckCommand command = new AbstractDuckCommand() {
                @Override
                public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
                    expansions.get(c).sendMessage(sender);
                    return false;
                }
            };
            command.register(c, "/" + c, "Custom Text", "", null, "");
        });
    }

    public CompletableFuture<AddonsMain> loadFiles(AddonsMain main) {
        return CompletableFuture.supplyAsync(() -> {
            File folder = new File(main.getDataFolder() + separator + "addons" + separator + "customtext");
            if (!folder.exists()) folder.mkdirs();
            if (folder.isDirectory()) {
                for (File file : folder.listFiles()) {
                    final Class<? extends CustomTextAddon> expansionClass = main.findClass(file, CustomTextAddon.class);

                    if (expansionClass == null) {
                        Bukkit.getLogger().severe("Failed to load expansion" + file.getName() + ", as it does not " +
                                "have a class which extends CustomTextAddon");
                        continue;
                    }
                    try {
                        CustomTextAddon expansion = expansionClass.getDeclaredConstructor().newInstance();
                        expansions.put(expansion.command(), expansion);
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
