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
import java.util.concurrent.atomic.AtomicInteger;

public class Countdowner {
    private final Map<UUID, Integer> repeatMap = new ConcurrentHashMap<>();

    public static void start(@NotNull final AbstractPlugin plugin, final boolean async, final int repeat, final long delay, final long period, @Nullable final CountdownerStartHandler countdownerStartHandler, @Nullable final CountdownerRunHandler countdownerRunHandler, @Nullable final CountdownerStopHandler countdownerStopHandler) {
        final AtomicInteger atomicInteger = new AtomicInteger(repeat);
        if (countdownerStartHandler == null || countdownerStartHandler.handle()) {
            final BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if ((atomicInteger.getAndDecrement() <= 0 || (countdownerRunHandler != null && !countdownerRunHandler.handle(atomicInteger.get()))) && (countdownerStopHandler == null || countdownerStopHandler.handle())) {
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

    public final void start(@NotNull final AbstractPlugin plugin, @NotNull final Entity entity, final boolean async, final int repeat, final long delay, final long period, @Nullable final CountdownerStartHandler countdownerStartHandler, @Nullable final CountdownerRunHandler countdownerRunHandler, @Nullable final CountdownerStopHandler countdownerStopHandler) {
        final UUID uuid = entity.getUniqueId();
        if (!isRunning(uuid)) {
            if (countdownerStartHandler == null || countdownerStartHandler.handle()) {
                repeatMap.put(uuid, repeat);
                final BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        final int repeatLeft = getRepeatLeft(uuid);
                        if (repeatLeft > 0) {
                            if (countdownerRunHandler == null || countdownerRunHandler.handle(repeatLeft)) {
                                repeatMap.put(uuid, Math.max(repeatLeft - 1, 0));
                            }
                        } else if (countdownerStopHandler == null || countdownerStopHandler.handle()) {
                            repeatMap.remove(uuid);
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

    public final void reentry(@NotNull final AbstractPlugin plugin, @NotNull final Entity entity, final boolean async, final int repeat, final long delay, final long period, @Nullable final CountdownerReentryHandler countdownerReentryHandler, @Nullable final CountdownerStartHandler countdownerStartHandler, @Nullable final CountdownerRunHandler countdownerRunHandler, @Nullable final CountdownerStopHandler countdownerStopHandler) {
        final UUID uuid = entity.getUniqueId();
        if (isRunning(uuid)) {
            if (countdownerReentryHandler == null || countdownerReentryHandler.handle()) {
                repeatMap.merge(uuid, repeat, Integer::max);
            }
        } else {
            if (countdownerStartHandler == null || countdownerStartHandler.handle()) {
                repeatMap.put(uuid, repeat);
                final BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        final int repeatLeft = getRepeatLeft(uuid);
                        if (repeatLeft > 0) {
                            if (countdownerRunHandler == null || countdownerRunHandler.handle(repeatLeft)) {
                                repeatMap.put(uuid, Math.max(repeatLeft - 1, 0));
                            }
                        } else if (countdownerStopHandler == null || countdownerStopHandler.handle()) {
                            repeatMap.remove(uuid);
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
        repeatMap.remove(uuid);
    }

    public final void stop(@NotNull final Entity entity) {
        stop(entity.getUniqueId());
    }

    public final @NotNull Set<UUID> getRunnings() {
        final Set<UUID> runningSet = new HashSet<>();
        for (final Map.Entry<UUID, Integer> entry : repeatMap.entrySet()) {
            if (entry.getValue() > 0) {
                runningSet.add(entry.getKey());
            }
        }
        return runningSet;
    }

    public final int getRepeatLeft(@NotNull final UUID uuid) {
        return repeatMap.getOrDefault(uuid, 0);
    }

    public final int getRepeatLeft(@NotNull final Entity entity) {
        return getRepeatLeft(entity.getUniqueId());
    }

    public final boolean isRunning(@NotNull final UUID uuid) {
        final Integer repeat = repeatMap.get(uuid);
        return repeat != null && repeat > 0;
    }

    public final boolean isRunning(@NotNull final Entity entity) {
        return isRunning(entity.getUniqueId());
    }
}
