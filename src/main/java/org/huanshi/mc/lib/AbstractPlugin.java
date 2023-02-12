package org.huanshi.mc.lib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.huanshi.mc.lib.core.Loader;
import org.huanshi.mc.lib.lang.Zh;

import java.io.File;

public abstract class AbstractPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        File dataFolder = getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            setEnabled(false);
        }
        try {
            Loader.load(this);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
        enable();
        getServer().getConsoleSender().sendMessage(Zh.enable(getName()));
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        disable();
        getServer().getConsoleSender().sendMessage(Zh.disable(getName()));
    }

    protected void enable() {}

    protected void disable() {}
}
