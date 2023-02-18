package org.huanshi.mc.lib.task;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.lib.engine.Component;
import org.huanshi.mc.lib.engine.Registrable;

public abstract class AbstractTask extends BukkitRunnable implements Component, Registrable {
    @Override
    public void load() {}

    @Override
    public void onLoad() {}

    @Override
    public abstract void register();

    @Override
    public abstract void run();
}
