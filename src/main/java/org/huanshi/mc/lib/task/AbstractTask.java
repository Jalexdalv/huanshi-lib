package org.huanshi.mc.lib.task;

import org.bukkit.scheduler.BukkitRunnable;

public abstract class AbstractTask extends BukkitRunnable {
    public void load() {}

    @Override
    public abstract void run();

    public abstract void register();
}
