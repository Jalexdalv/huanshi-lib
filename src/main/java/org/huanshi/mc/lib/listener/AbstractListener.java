package org.huanshi.mc.lib.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.huanshi.mc.lib.AbstractPlugin;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.engine.Component;
import org.huanshi.mc.lib.engine.Registrable;

public abstract class AbstractListener implements Component, Registrable, Listener {
    @Autowired
    private AbstractPlugin plugin;

    @Override
    public void load() {}

    @Override
    public void onLoad() {}

    @Override
    public void register() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
