package org.huanshi.mc.lib.timer;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.lib.AbstractPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

public class Countdowner {
    public static void start(@NotNull AbstractPlugin plugin, boolean async, int repeat, int delay, int period, @Nullable CountdownerStartHandler countdownerStartHandler, @Nullable CountdownerRunHandler countdownerRunHandler, @Nullable CountdownerStopHandler countdownerStopHandler) {
        AtomicInteger atomicInteger = new AtomicInteger(repeat);
        if (countdownerStartHandler == null || countdownerStartHandler.handle()) {
            BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if ((atomicInteger.getAndDecrement() <= 0 || (countdownerRunHandler != null && !countdownerRunHandler.handle(atomicInteger.get()))) && (countdownerStopHandler == null || countdownerStopHandler.handle())) {
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
}
