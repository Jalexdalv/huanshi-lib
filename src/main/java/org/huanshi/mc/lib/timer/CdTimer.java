package org.huanshi.mc.lib.timer;

import org.bukkit.entity.Player;
import org.huanshi.mc.lib.lang.Zh;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CdTimer {
    private final Map<UUID, Long> finishTimeMap = new HashMap<>();

    public void run(@NotNull Player player, long cd, @Nullable TimerStartHandler timerStartHandler, @Nullable TimerRunHandler timerRunHandler) {
        UUID uuid = player.getUniqueId();
        int restTime = Math.max((int) Math.ceil((double) (finishTimeMap.getOrDefault(uuid, 0L) - System.currentTimeMillis()) / (double) 1000), 0);
        if (restTime > 0) {
            if (timerRunHandler != null) {
                timerRunHandler.handle(restTime);
            }
            player.sendActionBar(Zh.cd(restTime));
        } else if (timerStartHandler == null || timerStartHandler.handle()) {
            finishTimeMap.put(uuid, System.currentTimeMillis() + cd);
        }
    }

    public void run(@NotNull Player player, long cd, boolean restart, @Nullable TimerReStartHandler timerReStartHandler, @Nullable TimerStartHandler timerStartHandler, @Nullable TimerRunHandler timerRunHandler) {
        UUID uuid = player.getUniqueId();
        int restTime = Math.max((int) Math.ceil((double) (finishTimeMap.getOrDefault(uuid, 0L) - System.currentTimeMillis()) / (double) 1000), 0);
        if (restTime > 0) {
            if (restart) {
                if (timerReStartHandler == null || timerReStartHandler.handle()) {
                    finishTimeMap.merge(uuid, System.currentTimeMillis() + cd, Math::max);
                }
            } else {
                if (timerRunHandler != null) {
                    timerRunHandler.handle(restTime);
                }
                player.sendActionBar(Zh.cd(restTime));
            }
        } else if (timerStartHandler == null || timerStartHandler.handle()) {
            finishTimeMap.put(uuid, System.currentTimeMillis() + cd);
        }
    }

    public void clear(@NotNull UUID uuid) {
        finishTimeMap.remove(uuid);
    }

    public @NotNull Set<UUID> getRunnings() {
        Set<UUID> runningSet = new HashSet<>();
        for (Map.Entry<UUID, Long> entry : finishTimeMap.entrySet()) {
            if (entry.getValue() - System.currentTimeMillis() > 0) {
                runningSet.add(entry.getKey());
            }
        }
        return runningSet;
    }

    public boolean isRunning(@NotNull UUID uuid) {
        return finishTimeMap.getOrDefault(uuid, 0L) - System.currentTimeMillis() > 0;
    }
}
