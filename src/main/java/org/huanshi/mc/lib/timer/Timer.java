package org.huanshi.mc.lib.timer;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.lib.AbstractPlugin;
import org.huanshi.mc.lib.utils.TimerUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Timer {
    private final Map<UUID, Integer> repeatMap = new ConcurrentHashMap<>();

    public void run(@NotNull AbstractPlugin plugin, @NotNull UUID uuid, boolean async, boolean reentry, long duration, int delay, int period, @Nullable TimerReentryHandler timerReentryHandler, @Nullable TimerStartHandler timerStartHandler, @Nullable TimerRunHandler timerRunHandler, @Nullable TimerFinishHandler timerFinishHandler) {
        if (isRunning(uuid)) {
            if (reentry && (timerReentryHandler == null || timerReentryHandler.handle())) {
                repeatMap.merge(uuid, TimerUtils.convertMillisecondToRepeat(duration, period), Integer::max);
            }
        } else if (timerStartHandler == null || timerStartHandler.handle()) {
            repeatMap.put(uuid, TimerUtils.convertMillisecondToRepeat(duration, period));
            BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                @Override
                public void run() {
                    Integer remainRepeat = repeatMap.getOrDefault(uuid, 0);
                    if (remainRepeat > 0) {
                        if (timerRunHandler != null) {
                            timerRunHandler.handle(TimerUtils.convertRepeatToMillisecond(remainRepeat, period));
                        }
                        repeatMap.put(uuid, remainRepeat - 1);
                    } else if (timerFinishHandler == null || timerFinishHandler.handle()) {
                        repeatMap.remove(uuid);
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

    public void clear(@NotNull UUID uuid) {
        repeatMap.remove(uuid);
    }

    public @NotNull Set<UUID> getRunnings() {
        Set<UUID> set = new HashSet<>();
        for (Map.Entry<UUID, Integer> entry : repeatMap.entrySet()) {
            if (entry.getValue() > 0) {
                set.add(entry.getKey());
            }
        }
        return set;
    }

    public boolean isRunning(@NotNull UUID uuid) {
        return repeatMap.getOrDefault(uuid, 0) > 0;
    }
}
