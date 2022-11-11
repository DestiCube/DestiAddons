package com.desticube.addons;

import com.desticube.addons.api.customtext.CustomTexts;
import com.desticube.addons.api.inventories.Inventories;
import com.desticube.addons.api.listeners.Listeners;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class AddonsMain extends JavaPlugin {

    public static JavaPlugin PLUGIN;

    private final Inventories invs = new Inventories();
    private final CustomTexts customText = new CustomTexts();
    private final Listeners listeners = new Listeners();

    @Override
    public void onEnable() {
        PLUGIN = this;
        invs.loadFiles(this);
        invs.register(this);
        customText.loadFiles(this);
        customText.register(this);
        listeners.loadFiles(this);
        listeners.register(this);
    }


    public <T> Class<? extends T> findClass(@NotNull final File file,
                                                   @NotNull final Class<T> clazz)  {
        try {
            if (!file.exists()) {
                return null;
            }

            final URL jar = file.toURI().toURL();
            final URLClassLoader loader = new URLClassLoader(new URL[]{jar}, clazz.getClassLoader());
            final List<String> matches = new ArrayList<>();
            final List<Class<? extends T>> classes = new ArrayList<>();

            try (final JarInputStream stream = new JarInputStream(jar.openStream())) {
                JarEntry entry;
                while ((entry = stream.getNextJarEntry()) != null) {
                    final String name = entry.getName();
                    if (name.isEmpty() || !name.endsWith(".class")) {
                        continue;
                    }

                    matches.add(name.substring(0, name.lastIndexOf('.')).replace('/', '.'));
                }

                for (final String match : matches) {
                    try {
                        final Class<?> loaded = loader.loadClass(match);
                        if (clazz.isAssignableFrom(loaded)) {
                            classes.add(loaded.asSubclass(clazz));
                        }
                    } catch (final NoClassDefFoundError ignored) {
                    }
                }
            }
            if (classes.isEmpty()) {
                loader.close();
                return null;
            }
            return classes.get(0);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
