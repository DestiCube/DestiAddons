package com.desticube.addons.api;

import com.desticube.addons.AddonsMain;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface AddonManager {

    public CompletableFuture<AddonsMain> loadFiles(AddonsMain main);

    public void register(AddonsMain main);
}
