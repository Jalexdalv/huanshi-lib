package org.huanshi.mc.lib.timer;

import org.bukkit.entity.Entity;
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

    public final void start(@NotNull final Entity entity, final long duration, @Nullable final CooldownerStartHandler cooldownerStartHandler, @Nullable final CooldownerRunHandler cooldownerRunHandler) {
        final UUID uuid = entity.getUniqueId();
        final long durationLeft = getDurationLeft(uuid);
        if (durationLeft > 0) {
            if (cooldownerRunHandler == null || cooldownerRunHandler.handle(durationLeft)) {
                entity.sendActionBar(Zh.cd(durationLeft));
            }
        } else if (cooldownerStartHandler == null || cooldownerStartHandler.handle()) {
            timeMap.put(uuid, System.currentTimeMillis() + duration);
        }
    }

    public final void reentry(@NotNull final Entity entity, final long duration, @Nullable final CooldownerReentryHandler cooldownerReentryHandler, @Nullable final CooldownerStartHandler cooldownerStartHandler) {
        final UUID uuid = entity.getUniqueId();
        if (isRunning(uuid)) {
            if (cooldownerReentryHandler == null || cooldownerReentryHandler.handle()) {
                timeMap.merge(uuid, System.currentTimeMillis() + duration, Long::max);
            }
        } else if (cooldownerStartHandler == null || cooldownerStartHandler.handle()) {
            timeMap.put(uuid, System.currentTimeMillis() + duration);
        }
    }

    public final void stop(@NotNull final UUID uuid) {
        timeMap.remove(uuid);
    }

    public final void stop(@NotNull final Entity entity) {
        stop(entity.getUniqueId());
    }

    public final @NotNull Set<UUID> getRunnings() {
        final Set<UUID> runningSet = new HashSet<>();
        for (final Map.Entry<UUID, Long> entry : timeMap.entrySet()) {
            if (entry.getValue() - System.currentTimeMillis() > 0) {
                runningSet.add(entry.getKey());
            }
        }
        return runningSet;
    }

    public final long getDurationLeft(@NotNull final UUID uuid) {
        Long time = timeMap.get(uuid);
        return time == null ? 0L : Math.max(time - System.currentTimeMillis(), 0);
    }

    public final long getDurationLeft(@NotNull final Entity entity) {
        return getDurationLeft(entity.getUniqueId());
    }

    public final boolean isRunning(@NotNull final UUID uuid) {
        final Long time = timeMap.get(uuid);
        return time != null && time - System.currentTimeMillis() > 0;
    }

    public final boolean isRunning(@NotNull final Entity entity) {
        return isRunning(entity.getUniqueId());
    }
}
