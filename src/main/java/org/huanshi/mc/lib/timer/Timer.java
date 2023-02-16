package org.huanshi.mc.lib.timer;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.lib.AbstractPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Timer {
    private final Map<UUID, Long> durationMap = new ConcurrentHashMap<>();

    public void start(@NotNull AbstractPlugin plugin, @NotNull Player player, boolean async, long duration, int delay, int period, @Nullable TimerStartHandler timerStartHandler, @Nullable TimerRunHandler timerRunHandler, @Nullable TimerStopHandler timerStopHandler) {
        UUID uuid = player.getUniqueId();
        if (!isRunning(uuid)) {
            if (timerStartHandler == null || timerStartHandler.handle()) {
                durationMap.put(uuid, duration);
                BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        long durationLeft = getDurationLeft(uuid);
                        if (durationLeft > 0) {
                            if (timerRunHandler == null || timerRunHandler.handle(durationLeft)) {
                                durationMap.put(uuid, Math.max(durationLeft - 50L * period, 0L));
                            }
                        } else if (timerStopHandler == null || timerStopHandler.handle()) {
                            durationMap.remove(uuid);
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

    public void reentry(@NotNull AbstractPlugin plugin, @NotNull Player player, boolean async, long duration, int delay, int period, @Nullable TimerReentryHandler timerReentryHandler, @Nullable TimerStartHandler timerStartHandler, @Nullable TimerRunHandler timerRunHandler, @Nullable TimerStopHandler timerStopHandler) {
        UUID uuid = player.getUniqueId();
        if (isRunning(uuid)) {
            if (timerReentryHandler == null || timerReentryHandler.handle()) {
                durationMap.merge(uuid, duration, Long::max);
            }
        } else {
            if (timerStartHandler == null || timerStartHandler.handle()) {
                durationMap.put(uuid, duration);
                BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        long durationLeft = getDurationLeft(uuid);
                        if (durationLeft > 0) {
                            if (timerRunHandler == null || timerRunHandler.handle(durationLeft)) {
                                durationMap.put(uuid, Math.max(durationLeft - 50L * period, 0L));
                            }
                        } else if (timerStopHandler == null || timerStopHandler.handle()) {
                            durationMap.remove(uuid);
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

    public void stop(@NotNull UUID uuid) {
        durationMap.remove(uuid);
    }

    public void stop(@NotNull Player player) {
        stop(player.getUniqueId());
    }

    public @NotNull Set<UUID> getRunnings() {
        Set<UUID> runningSet = new HashSet<>();
        for (Map.Entry<UUID, Long> entry : durationMap.entrySet()) {
            if (entry.getValue() > 0) {
                runningSet.add(entry.getKey());
            }
        }
        return runningSet;
    }

    public long getDurationLeft(@NotNull UUID uuid) {
        return durationMap.getOrDefault(uuid, 0L);
    }

    public long getDurationLeft(@NotNull Player player) {
        return getDurationLeft(player.getUniqueId());
    }

    public boolean isRunning(@NotNull UUID uuid) {
        Long duration = durationMap.get(uuid);
        return duration != null && duration > 0;
    }

    public boolean isRunning(@NotNull Player player) {
        return isRunning(player.getUniqueId());
    }
}
