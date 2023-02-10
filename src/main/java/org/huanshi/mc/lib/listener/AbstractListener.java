package org.huanshi.mc.lib.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.huanshi.mc.lib.AbstractPlugin;
import org.huanshi.mc.lib.annotation.Autowired;

/**
 * 抽象监听器
 * @author Jalexdalv
 */
public abstract class AbstractListener implements Listener {
    @Autowired
    private AbstractPlugin plugin;

    /**
     * 加载
     */
    public void load() {}

    /**
     * 注册
     */
    public void register() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
