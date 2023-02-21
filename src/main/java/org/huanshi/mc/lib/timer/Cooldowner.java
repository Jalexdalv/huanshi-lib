package org.huanshi.mc.lib.timer;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.huanshi.mc.lib.lang.Zh;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Cooldowner {
    private final Map<UUID, Long> timeMap = new HashMap<>();

    public void start(@NotNull Entity entity, long duration, @Nullable CooldownerStartHandler cooldownerStartHandler, @Nullable CooldownerRunHandler cooldownerRunHandler) {
        UUID uuid = entity.getUniqueId();
        long durationLeft = getDurationLeft(uuid);
        if (durationLeft > 0) {
            if ((cooldownerRunHandler == null || cooldownerRunHandler.handle(durationLeft)) && entity instanceof Player player) {
                player.sendActionBar(Zh.cd(durationLeft));
            }
        } else if (cooldownerStartHandler == null || cooldownerStartHandler.handle()) {
            timeMap.put(uuid, System.currentTimeMillis() + duration);
        }
    }

    public void reentry(@NotNull Entity entity, long duration, @Nullable CooldownerReentryHandler cooldownerReentryHandler, @Nullable CooldownerStartHandler cooldownerStartHandler) {
        UUID uuid = entity.getUniqueId();
        if (isRunning(uuid)) {
            if (cooldownerReentryHandler == null || cooldownerReentryHandler.handle()) {
                timeMap.merge(uuid, System.currentTimeMillis() + duration, Long::max);
            }
        } else if (cooldownerStartHandler == null || cooldownerStartHandler.handle()) {
            timeMap.put(uuid, System.currentTimeMillis() + duration);
        }
    }

    public void stop(@NotNull UUID uuid) {
        timeMap.remove(uuid);
    }

    public void stop(@NotNull Entity entity) {
        stop(entity.getUniqueId());
    }

    public @NotNull Set<UUID> getRunnings() {
        Set<UUID> runningSet = new HashSet<>();
        for (Map.Entry<UUID, Long> entry : timeMap.entrySet()) {
            if (entry.getValue() - System.currentTimeMillis() > 0) {
                runningSet.add(entry.getKey());
            }
        }
        return runningSet;
    }

    public long getDurationLeft(@NotNull UUID uuid) {
        Long time = timeMap.get(uuid);
        return time == null ? 0L : Math.max(time - System.currentTimeMillis(), 0);
    }

    public long getDurationLeft(@NotNull Entity entity) {
        return getDurationLeft(entity.getUniqueId());
    }

    public boolean isRunning(@NotNull UUID uuid) {
        Long time = timeMap.get(uuid);
        return time != null && time - System.currentTimeMillis() > 0;
    }

    public boolean isRunning(@NotNull Entity entity) {
        return isRunning(entity.getUniqueId());
    }
}
