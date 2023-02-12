package org.huanshi.mc.lib.utils;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.lib.AbstractPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

public class TimerUtils {
    public static void repeat(@NotNull AbstractPlugin plugin, boolean async, int repeat, int delay, int period, @Nullable RepeatStartHandler repeatStartHandler, @Nullable RepeatRunHandler repeatRunHandler, @Nullable RepeatFinishHandler repeatFinishHandler) {
        AtomicInteger atomicInteger = new AtomicInteger(repeat);
        if (repeatStartHandler == null || repeatStartHandler.handle()) {
            BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if ((atomicInteger.getAndDecrement() <= 0 || (repeatRunHandler != null && !repeatRunHandler.handle(atomicInteger.get()))) && (repeatFinishHandler == null || repeatFinishHandler.handle())) {
                        cancel();
                    }
                }
            };
            if (async) {
                bukkitRunnable.runTaskTimerAsynchronously(plugin, delay, period);
            } else {
                bukkitRunnable.runTaskTimer(plugin, delay, period);
            }
        }
    }

    public static int convertMillisecondToTick(long millisecond) {
        return (int) Math.ceil((double) millisecond / (double) 50);
    }

    public static int convertMillisecondToRepeat(long millisecond, int period) {
        return (int) Math.ceil((double) millisecond / (double) 50 / (double) period);
    }

    public static long convertRepeatToMillisecond(int repeat, int period) {
        return (long) repeat * period * 50;
    }

    public static int convertMillisecondToSecond(long millisecond) {
        return (int) Math.ceil((double) millisecond / (double) 1000);
    }
}
