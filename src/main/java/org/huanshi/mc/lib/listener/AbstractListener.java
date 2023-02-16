package org.huanshi.mc.lib.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.huanshi.mc.lib.AbstractPlugin;
import org.huanshi.mc.lib.Component;
import org.huanshi.mc.lib.annotation.Autowired;

public abstract class AbstractListener implements Component, Listener {
    @Autowired
    private AbstractPlugin plugin;

    public void load() {}

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
