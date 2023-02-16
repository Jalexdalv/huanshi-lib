package org.huanshi.mc.lib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.huanshi.mc.lib.lang.Zh;

import java.nio.file.Files;
import java.nio.file.Path;

public abstract class AbstractPlugin extends JavaPlugin implements Component {
    public void load() {}

    protected void start() {}

    protected void stop() {}

    @Override
    public void onEnable() {
        try {
            Path path = getDataFolder().toPath();
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
            Loader.load(this);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
        start();
        Bukkit.getConsoleSender().sendMessage(Zh.enable(getName()));
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        stop();
        Bukkit.getConsoleSender().sendMessage(Zh.disable(getName()));
    }
}
