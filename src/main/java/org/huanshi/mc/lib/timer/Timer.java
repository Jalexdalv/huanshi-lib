package org.huanshi.mc.lib.timer;

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.lib.utils.FormatUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Timer {
    private final Map<UUID, Long> durationMap = new ConcurrentHashMap<>();

    public final void start(@NotNull final AbstractPlugin plugin, @NotNull final Entity entity, final boolean async, final long duration, final long delay, final long period, @Nullable final TimerStartHandler timerStartHandler, @Nullable final TimerRunHandler timerRunHandler, @Nullable final TimerStopHandler timerStopHandler) {
        final UUID uuid = entity.getUniqueId();
        if (!isRunning(uuid)) {
            if (timerStartHandler == null || timerStartHandler.handle()) {
                durationMap.put(uuid, duration);
                final BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        final long durationLeft = getDurationLeft(uuid);
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
                long delayTick = FormatUtils.convertDurationToTick(delay), periodTick = FormatUtils.convertDurationToTick(period);
                if (async) {
                    bukkitRunnable.runTaskTimerAsynchronously(plugin, delayTick, periodTick);
                } else {
                    bukkitRunnable.runTaskTimer(plugin, delayTick, periodTick);
                }
            }
        }
    }

    public final void reentry(@NotNull final AbstractPlugin plugin, @NotNull final Entity entity, final boolean async, final long duration, final long delay, final long period, @Nullable final TimerReentryHandler timerReentryHandler, @Nullable final TimerStartHandler timerStartHandler, @Nullable final TimerRunHandler timerRunHandler, @Nullable final TimerStopHandler timerStopHandler) {
        final UUID uuid = entity.getUniqueId();
        if (isRunning(uuid)) {
            if (timerReentryHandler == null || timerReentryHandler.handle()) {
                durationMap.merge(uuid, duration, Long::max);
            }
        } else {
            if (timerStartHandler == null || timerStartHandler.handle()) {
                durationMap.put(uuid, duration);
                final BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        final long durationLeft = getDurationLeft(uuid);
                        if (durationLeft > 0) {
                            if (timerRunHandler == null || timerRunHandler.handle(durationLeft)) {
                                durationMap.put(uuid, Math.max(durationLeft - period, 0L));
                            }
                        } else if (timerStopHandler == null || timerStopHandler.handle()) {
                            durationMap.remove(uuid);
                            cancel();
                        }
                    }
                };
                long delayTick = FormatUtils.convertDurationToTick(delay), periodTick = FormatUtils.convertDurationToTick(period);
                if (async) {
                    bukkitRunnable.runTaskTimerAsynchronously(plugin, delayTick, periodTick);
                } else {
                    bukkitRunnable.runTaskTimer(plugin, delayTick, periodTick);
                }
            }
        }
    }

    public final void stop(@NotNull final UUID uuid) {
        durationMap.remove(uuid);
    }

    public final void stop(@NotNull final Entity entity) {
        stop(entity.getUniqueId());
    }

    public final @NotNull Set<UUID> getRunnings() {
        final Set<UUID> runningSet = new HashSet<>();
        for (final Map.Entry<UUID, Long> entry : durationMap.entrySet()) {
            if (entry.getValue() > 0) {
                runningSet.add(entry.getKey());
            }
        }
        return runningSet;
    }

    public final long getDurationLeft(@NotNull final UUID uuid) {
        return durationMap.getOrDefault(uuid, 0L);
    }

    public final long getDurationLeft(@NotNull final Entity entity) {
        return getDurationLeft(entity.getUniqueId());
    }

    public final boolean isRunning(@NotNull final UUID uuid) {
        final Long duration = durationMap.get(uuid);
        return duration != null && duration > 0;
    }

    public final boolean isRunning(@NotNull final Entity entity) {
        return isRunning(entity.getUniqueId());
    }
}
