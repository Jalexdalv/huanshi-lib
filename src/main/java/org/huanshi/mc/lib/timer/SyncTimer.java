package org.huanshi.mc.lib.timer;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.lib.AbstractPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SyncTimer {
    private final Map<UUID, Integer> restTimeMap = new ConcurrentHashMap<>();

    public void run(@NotNull UUID uuid, @NotNull AbstractPlugin plugin, int cd, long period, boolean isRestart, boolean isOverlying, @Nullable TimerRestartStartHandler timerRestartHandler, @Nullable TimerStartHandler timerStartHandler, @Nullable TimerRunHandler timerRunHandler, @Nullable TimerFinishHandler timerFinishHandler) {
        if (restTimeMap.containsKey(uuid)) {
            if (isRestart && (timerRestartHandler == null || timerRestartHandler.handle())) {
                if (isOverlying) {
                    restTimeMap.merge(uuid, cd, Integer::sum);
                } else {
                    restTimeMap.put(uuid, cd);
                }
            }
        } else if (timerStartHandler == null || timerStartHandler.handle()) {
            restTimeMap.put(uuid, cd);
            new BukkitRunnable() {
                @Override
                public void run() {
                    Integer restTime = restTimeMap.getOrDefault(uuid, 0);
                    if (restTime == 0) {
                        if (timerFinishHandler == null || timerFinishHandler.handle()) {
                            restTimeMap.remove(uuid);
                            cancel();
                        }
                    } else if (timerRunHandler == null || timerRunHandler.handle(restTime)) {
                        restTimeMap.put(uuid, restTime - 1);
                    }
                }
            }.runTaskTimer(plugin, 0L, period);
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
