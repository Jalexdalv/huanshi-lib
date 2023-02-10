package org.huanshi.mc.lib.timer;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.lib.AbstractPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SyncTimer {
    private final Map<UUID, Integer> restTimeMap = new HashMap<>();

    public void run(@NotNull UUID uuid, @NotNull AbstractPlugin plugin, long time, long period, @Nullable TimerStartHandler timerStartHandler, @Nullable TimerRunHandler timerRunHandler, @Nullable TimerFinishHandler timerFinishHandler) {
        if (!isRunning(uuid) && (timerStartHandler == null || timerStartHandler.handle())) {
            restTimeMap.put(uuid, (int) Math.round((double) time / (double) period));
            new BukkitRunnable() {
                @Override
                public void run() {
                    Integer restTime = restTimeMap.getOrDefault(uuid, 0);
                    if (restTime > 0) {
                        if (timerRunHandler != null) {
                            timerRunHandler.handle(restTime);
                        }
                        restTimeMap.put(uuid, restTime - 1);
                    } else if (timerFinishHandler == null || timerFinishHandler.handle()) {
                        restTimeMap.remove(uuid);
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0L, Math.round((double) period / (double) 50));
        }
    }

    public void run(@NotNull UUID uuid, @NotNull AbstractPlugin plugin, long time, long period, boolean restart, @Nullable TimerReStartHandler timerReStartHandler, @Nullable TimerStartHandler timerStartHandler, @Nullable TimerRunHandler timerRunHandler, @Nullable TimerFinishHandler timerFinishHandler) {
        if (isRunning(uuid)) {
            if (restart && (timerReStartHandler == null || timerReStartHandler.handle())) {
                restTimeMap.merge(uuid, (int) Math.round((double) time / (double) period), Integer::max);
            }
        } else if (timerStartHandler == null || timerStartHandler.handle()) {
            restTimeMap.put(uuid, (int) Math.round((double) time / (double) period));
            new BukkitRunnable() {
                @Override
                public void run() {
                    Integer restTime = restTimeMap.getOrDefault(uuid, 0);
                    if (restTime > 0) {
                        if (timerRunHandler != null) {
                            timerRunHandler.handle(restTime);
                        }
                        restTimeMap.put(uuid, restTime - 1);
                    } else if (timerFinishHandler == null || timerFinishHandler.handle()) {
                        restTimeMap.remove(uuid);
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0L, Math.round((double) period / (double) 50));
        }
    }

    public void clear(@NotNull UUID uuid) {
        restTimeMap.remove(uuid);
    }

    public @NotNull Set<UUID> getRunnings() {
        Set<UUID> runningSet = new HashSet<>();
        for (Map.Entry<UUID, Integer> entry : restTimeMap.entrySet()) {
            if (entry.getValue() > 0) {
                runningSet.add(entry.getKey());
            }
        }
        return runningSet;
    }

    public boolean isRunning(@NotNull UUID uuid) {
        return restTimeMap.getOrDefault(uuid, 0) > 0;
    }
}
