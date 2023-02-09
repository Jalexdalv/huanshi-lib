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
    private static final SyncTimer STUN_TIMER = new SyncTimer();
    private static final CdTimer ROOT_TIMER = new CdTimer(), SILENCE_TIMER = new CdTimer(), STEADY_TIMER = new CdTimer(), INVINCIBLE_TIMER = new CdTimer();
    private static final Vector STIFF_VECTOR = new Vector(0, 0, 0), STRIKE_FLY_VECTOR = new Vector(0, 1, 0);

    public static void stun(@NotNull Player player, @NotNull AbstractPlugin plugin, int time, boolean force) {
        UUID uuid = player.getUniqueId();
        STUN_TIMER.run(player.getUniqueId(), plugin, time, 1L, true, true,
            () -> force || !isSteadying(uuid),
            () -> {
                if (force || !isSteadying(uuid)) {
                    player.setWalkSpeed(0);
                    return true;
                }
                return false;
            },
            restTime -> {
                player.setRotation(0, 90);
                return true;
            },
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

    public static void root(@NotNull Player player, int time, boolean ignore) {
        UUID uuid = player.getUniqueId();
        ROOT_TIMER.run(player, time, true, true,
            () -> ignore || !isSteadying(uuid),
            () -> {
                if (ignore || !isSteadying(uuid)) {
                    player.setWalkSpeed(0);
                    return true;
                }
                return false;
            },
            null
        );
    }

    public static void clearRoot(@NotNull UUID uuid) {
        ROOT_TIMER.clear(uuid);
    }

    public static boolean isRooting(@NotNull UUID uuid) {
        return ROOT_TIMER.isRunning(uuid);
    }

    public static void silence(@NotNull Player player, int time) {
        SILENCE_TIMER.run(player, time, true, true, null, null, null);
    }

    public static void clearSilence(@NotNull UUID uuid) {
        SILENCE_TIMER.clear(uuid);
    }

    public static boolean isSilencing(@NotNull UUID uuid) {
        return SILENCE_TIMER.isRunning(uuid);
    }

    public static void steady(@NotNull Player player, int time) {
        STEADY_TIMER.run(player, time, true, true, null, null, null);
    }

    public static void clearSteady(@NotNull UUID uuid) {
        STEADY_TIMER.clear(uuid);
    }

    public static boolean isSteadying(@NotNull UUID uuid) {
        return STEADY_TIMER.isRunning(uuid);
    }

    public static void invincible(@NotNull Player player, int time) {
        INVINCIBLE_TIMER.run(player, time, true, true, null, null, null);
    }

    public static void clearInvincible(@NotNull UUID uuid) {
        INVINCIBLE_TIMER.clear(uuid);
    }

    public static boolean isInvincibling(@NotNull UUID uuid) {
        return INVINCIBLE_TIMER.isRunning(uuid);
    }

    public static void stiff(@NotNull LivingEntity livingEntity) {
        livingEntity.setVelocity(STIFF_VECTOR);
    }

    public static void strikeFly(@NotNull LivingEntity livingEntity, double velocity) {
        livingEntity.setVelocity(STRIKE_FLY_VECTOR.clone().multiply(velocity));
    }
}
