package org.huanshi.mc.lib.timer;

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.lib.AbstractPlugin;
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

    public void start(@NotNull AbstractPlugin plugin, @NotNull Entity entity, boolean async, int repeat, int delay, int period, @Nullable CountdownerStartHandler countdownerStartHandler, @Nullable CountdownerRunHandler countdownerRunHandler, @Nullable CountdownerStopHandler countdownerStopHandler) {
        UUID uuid = entity.getUniqueId();
        if (!isRunning(uuid)) {
            if (countdownerStartHandler == null || countdownerStartHandler.handle()) {
                repeatMap.put(uuid, repeat);
                BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        int repeatLeft = getRepeatLeft(uuid);
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
                if (async) {
                    bukkitRunnable.runTaskTimerAsynchronously(plugin, delay, period);
                } else {
                    bukkitRunnable.runTaskTimer(plugin, delay, period);
                }
            }
        }
    }

    public void reentry(@NotNull AbstractPlugin plugin, @NotNull Entity entity, boolean async, int repeat, int delay, int period, @Nullable CountdownerReentryHandler countdownerReentryHandler, @Nullable CountdownerStartHandler countdownerStartHandler, @Nullable CountdownerRunHandler countdownerRunHandler, @Nullable CountdownerStopHandler countdownerStopHandler) {
        UUID uuid = entity.getUniqueId();
        if (isRunning(uuid)) {
            if (countdownerReentryHandler == null || countdownerReentryHandler.handle()) {
                repeatMap.merge(uuid, repeat, Integer::max);
            }
        } else {
            if (countdownerStartHandler == null || countdownerStartHandler.handle()) {
                repeatMap.put(uuid, repeat);
                BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        int repeatLeft = getRepeatLeft(uuid);
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
                if (async) {
                    bukkitRunnable.runTaskTimerAsynchronously(plugin, delay, period);
                } else {
                    bukkitRunnable.runTaskTimer(plugin, delay, period);
                }
            }
        }
    }

    public void stop(@NotNull UUID uuid) {
        repeatMap.remove(uuid);
    }

    public void stop(@NotNull Entity entity) {
        stop(entity.getUniqueId());
    }

    public @NotNull Set<UUID> getRunnings() {
        Set<UUID> runningSet = new HashSet<>();
        for (Map.Entry<UUID, Integer> entry : repeatMap.entrySet()) {
            if (entry.getValue() > 0) {
                runningSet.add(entry.getKey());
            }
        }
        return runningSet;
    }

    public int getRepeatLeft(@NotNull UUID uuid) {
        return repeatMap.getOrDefault(uuid, 0);
    }

    public int getRepeatLeft(@NotNull Entity entity) {
        return getRepeatLeft(entity.getUniqueId());
    }

    public boolean isRunning(@NotNull UUID uuid) {
        Integer repeat = repeatMap.get(uuid);
        return repeat != null && repeat > 0;
    }

    public boolean isRunning(@NotNull Entity entity) {
        return isRunning(entity.getUniqueId());
    }
}
