package org.huanshi.mc.lib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.huanshi.mc.lib.engine.Loader;
import org.huanshi.mc.lib.lang.Zh;

public abstract class AbstractPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        start();
        try {
            getDataFolder().mkdir();
            Loader.load(this);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
        Bukkit.getConsoleSender().sendMessage(Zh.enable(getName()));
    }

    @Override
    public void onDisable() {
        stop();
        Bukkit.getScheduler().cancelTasks(this);
        Bukkit.getConsoleSender().sendMessage(Zh.disable(getName()));
    }

    protected void start() {}

    protected void stop() {}
}
