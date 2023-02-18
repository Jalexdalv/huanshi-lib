package org.huanshi.mc.lib.config;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.huanshi.mc.lib.AbstractPlugin;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Config;
import org.huanshi.mc.lib.engine.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractConfig implements Component {
    @Autowired
    private AbstractPlugin plugin;
    private File file;
    private YamlConfiguration configuration;

    @Override
    public void load() throws IOException {
        Config config = getClass().getAnnotation(Config.class);
        file = new File(plugin.getDataFolder(), config.file());
        try (InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(plugin.getResource(config.file())))) {
            if (file.exists()) {
                configuration = YamlConfiguration.loadConfiguration(file);
                configuration.setDefaults(YamlConfiguration.loadConfiguration(inputStreamReader));
                configuration.options().copyDefaults(true);
            } else {
                configuration = YamlConfiguration.loadConfiguration(inputStreamReader);
            }
            save();
        }
    }

    @Override
    public void onLoad() {}

    public void save() throws IOException {
        configuration.save(file);
    }

    public void set(@NotNull String path, @Nullable Object value) {
        configuration.set(path, value);
    }

    public @NotNull Location getLocation(@NotNull String path) {
        return Objects.requireNonNull(configuration.getLocation(path));
    }

    public @NotNull String getString(@NotNull String path) {
        return Objects.requireNonNull(configuration.getString(path));
    }

    public @NotNull List<String> getStringList(@NotNull String path) {
        return configuration.getStringList(path);
    }

    public @NotNull Set<String> getStringSet(@NotNull String path) {
        return new HashSet<>(configuration.getStringList(path));
    }

    public long getLong(@NotNull String path) {
        return configuration.getLong(path);
    }

    public int getInt(@NotNull String path) {
        return configuration.getInt(path);
    }

    public double getDouble(@NotNull String path) {
        return configuration.getDouble(path);
    }

    public float getFloat(@NotNull String path) {
        return (float) configuration.getDouble(path);
    }
}
