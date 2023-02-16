package org.huanshi.mc.lib.task;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.lib.Component;

public abstract class AbstractTask extends BukkitRunnable implements Component {
    public void load() {}

    @Override
    public abstract void run();

    public abstract void register();
}
