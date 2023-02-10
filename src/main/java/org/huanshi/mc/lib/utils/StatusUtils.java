package org.huanshi.mc.lib.utils;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.huanshi.mc.lib.AbstractPlugin;
import org.huanshi.mc.lib.timer.CdTimer;
import org.huanshi.mc.lib.timer.SyncTimer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class StatusUtils {
    private static final SyncTimer STUN_TIMER = new SyncTimer(), ROOT_TIMER = new SyncTimer();
    private static final CdTimer SILENCE_TIMER = new CdTimer(), STEADY_TIMER = new CdTimer(), INVINCIBLE_TIMER = new CdTimer();
    private static final Vector STIFF_VECTOR = new Vector(0, 0, 0), STRIKE_FLY_VECTOR = new Vector(0, 1, 0);

    public static void stun(@NotNull Player player, @NotNull AbstractPlugin plugin, long time, boolean force) {
        UUID uuid = player.getUniqueId();
        STUN_TIMER.run(uuid, plugin, time, 1L, true,
            () -> force || !isSteadying(uuid),
            () -> {
                if (force || !isSteadying(uuid)) {
                    player.setWalkSpeed(0.0F);
                    return true;
                }
                return false;
            },
            restTime -> player.setRotation(0.0F, 90.0F),
            () -> {
                player.setWalkSpeed(0.2F);
                return true;
            }
        );
    }

    public static void clearStun(@NotNull UUID uuid) {
        STUN_TIMER.clear(uuid);
    }

    public static boolean isStunning(@NotNull UUID uuid) {
        return STUN_TIMER.isRunning(uuid);
    }

    public static void root(@NotNull Player player, @NotNull AbstractPlugin plugin, long time, boolean force) {
        UUID uuid = player.getUniqueId();
        ROOT_TIMER.run(uuid, plugin, time, time, true,
            () -> force || !isSteadying(uuid),
            () -> {
                if (force || !isSteadying(uuid)) {
                    player.setWalkSpeed(0.0F);
                    return true;
                }
                return false;
            }, null,
            () -> {
                player.setWalkSpeed(0.2F);
                return true;
            }
        );
    }

    public static void clearRoot(@NotNull UUID uuid) {
        ROOT_TIMER.clear(uuid);
    }

    public static boolean isRooting(@NotNull UUID uuid) {
        return ROOT_TIMER.isRunning(uuid);
    }

    public static void silence(@NotNull Player player, long time) {
        SILENCE_TIMER.run(player, time, true, null, null, null);
    }

    public static void clearSilence(@NotNull UUID uuid) {
        SILENCE_TIMER.clear(uuid);
    }

    public static boolean isSilencing(@NotNull UUID uuid) {
        return SILENCE_TIMER.isRunning(uuid);
    }

    public static void steady(@NotNull Player player, long time) {
        STEADY_TIMER.run(player, time, true, null, null, null);
    }

    public static void clearSteady(@NotNull UUID uuid) {
        STEADY_TIMER.clear(uuid);
    }

    public static boolean isSteadying(@NotNull UUID uuid) {
        return STEADY_TIMER.isRunning(uuid);
    }

    public static void invincible(@NotNull Player player, long time) {
        INVINCIBLE_TIMER.run(player, time, true, null, null, null);
    }

    public static void clearInvincible(@NotNull UUID uuid) {
        INVINCIBLE_TIMER.clear(uuid);
    }

    public static boolean isInvincibling(@NotNull UUID uuid) {
        return INVINCIBLE_TIMER.isRunning(uuid);
    }

    public static void stiff(@NotNull LivingEntity livingEntity, @NotNull AbstractPlugin plugin, long time) {
        ROOT_TIMER.run(livingEntity.getUniqueId(), plugin, time, 1L, true, null, null, restTime -> livingEntity.setVelocity(STIFF_VECTOR), null);
    }

    public static void strikeFly(@NotNull LivingEntity livingEntity, double velocity) {
        livingEntity.setVelocity(STRIKE_FLY_VECTOR.clone().multiply(velocity));
    }
}
