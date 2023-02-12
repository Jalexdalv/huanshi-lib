package org.huanshi.mc.lib.timer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CdTimer {
    private final Map<UUID, Long> cdMap = new HashMap<>();

    public void run(@NotNull UUID uuid, long duration, boolean reentry, @Nullable TimerReentryHandler timerReentryHandler, @Nullable TimerStartHandler timerStartHandler, @Nullable TimerRunHandler timerRunHandler) {
        long remainDuration = Math.max(cdMap.getOrDefault(uuid, 0L) - System.currentTimeMillis(), 0);
        if (remainDuration > 0) {
            if (reentry) {
                if (timerReentryHandler == null || timerReentryHandler.handle()) {
                    cdMap.merge(uuid, System.currentTimeMillis() + duration, Long::max);
                }
            } else {
                if (timerRunHandler != null) {
                    timerRunHandler.handle(remainDuration);
                }
            }
        } else if (timerStartHandler == null || timerStartHandler.handle()) {
            cdMap.put(uuid, System.currentTimeMillis() + duration);
        }
    }

    public void clear(@NotNull UUID uuid) {
        cdMap.remove(uuid);
    }

    public @NotNull Set<UUID> getRunnings() {
        Set<UUID> set = new HashSet<>();
        for (Map.Entry<UUID, Long> entry : cdMap.entrySet()) {
            if (entry.getValue() - System.currentTimeMillis() > 0) {
                set.add(entry.getKey());
            }
        }
        return set;
    }

    public boolean isRunning(@NotNull UUID uuid) {
        return cdMap.getOrDefault(uuid, 0L) - System.currentTimeMillis() > 0;
    }
}
