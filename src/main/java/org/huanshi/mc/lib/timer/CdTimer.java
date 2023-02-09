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

    public void run(@NotNull Player player, int cd, boolean isRestart, @Nullable TimerRestartStartHandler timerRestartHandler, @Nullable TimerStartHandler timerStartHandler, @Nullable TimerRunHandler timerRunHandler) {
        UUID uuid = player.getUniqueId();
        Long finishTime = finishTimeMap.get(uuid);
        if (finishTime != null) {
            if (isRestart && (timerRestartHandler == null || timerRestartHandler.handle())) {
                finishTimeMap.merge(uuid, System.currentTimeMillis() + cd * 1000L, Math::max);
                return;
            }
            int restTime = Math.max((int) Math.ceil((double) (finishTime - System.currentTimeMillis()) / (double) 1000), 0);
            if (restTime > 0) {
                if (timerRunHandler == null || timerRunHandler.handle(restTime)) {
                    player.sendActionBar(Zh.cd(restTime));
                }
                return;
            }
        }
        if (timerStartHandler == null || timerStartHandler.handle()) {
            finishTimeMap.put(uuid, System.currentTimeMillis() + cd * 1000L);
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
        Long finishTime = finishTimeMap.get(uuid);
        return finishTime != null && finishTimeMap.get(uuid) - System.currentTimeMillis() > 0;
    }
}
