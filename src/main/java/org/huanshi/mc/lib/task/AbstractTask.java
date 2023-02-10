package org.huanshi.mc.lib.task;

import org.bukkit.scheduler.BukkitRunnable;

/**
 * 抽象定时任务
 * @author Jalexdalv
 */
public abstract class AbstractTask extends BukkitRunnable {
    /**
     * 加载
     */
    public void load() {}

    /**
     * 运行
     */
    @Override
    public abstract void run();

    /**
     * 注册
     */
    public abstract void register();
}
