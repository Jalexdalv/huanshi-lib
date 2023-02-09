package org.huanshi.mc.lib;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.huanshi.mc.lib.core.Loader;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Set;

public abstract class AbstractPlugin extends JavaPlugin {
    @Override
    public abstract void onDisable();

    @Override
    public abstract void onEnable();

    protected void load() {
        try {
            Loader.scan(this);
            Loader.load(this);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public void reLoad() {
        try {
            Loader.load(this);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    protected void createFolder() {
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            if (!dataFolder.mkdirs()) {
                throw new RuntimeException("插件文件夹创建失败");
            }
        }
    }

    protected void clearAllTask() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    protected void sendConsoleMessage(@NotNull Component component) {
        getServer().getConsoleSender().sendMessage(component);
    }

    public boolean isCommand(@NotNull String command) {
        return Loader.isCommand(command);
    }

    public boolean isOpCommand(@NotNull String command) {
        return Loader.isOpCommand(command);
    }

    public @NotNull Set<String> getCommands() {
        return Loader.getCommands();
    }

    public @NotNull Set<String> getOpCommands() {
        return Loader.getOpCommands();
    }
}
