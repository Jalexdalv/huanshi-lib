package org.huanshi.mc.lib.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.huanshi.mc.lib.AbstractPlugin;
import org.huanshi.mc.lib.Component;
import org.huanshi.mc.lib.annotation.Autowired;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    public void load(@NotNull File file, @NotNull InputStream inputStream) throws IOException {
        this.file = file;
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
            if (file.exists()) {
                configuration = YamlConfiguration.loadConfiguration(file);
                configuration.setDefaults(YamlConfiguration.loadConfiguration(inputStreamReader));
                configuration.options().copyDefaults(true);
            } else {
                configuration = YamlConfiguration.loadConfiguration(inputStreamReader);
            }
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                save();
            } catch (IOException ioException) {
                throw new RuntimeException(ioException);
            }
        });
    }

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
