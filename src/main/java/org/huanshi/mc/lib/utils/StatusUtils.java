package org.huanshi.mc.lib.utils;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.huanshi.mc.lib.AbstractPlugin;
import org.huanshi.mc.lib.timer.CdTimer;
import org.huanshi.mc.lib.timer.Timer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * 状态工具类
 * @author Jalexdalv
 */
public class StatusUtils {
    private static final Timer STUN_TIMER = new Timer(), ROOT_TIMER = new Timer(), STIFF_TIMER = new Timer();
    private static final CdTimer SILENCE_TIMER = new CdTimer(), STEADY_TIMER = new CdTimer(), INVINCIBLE_TIMER = new CdTimer();
    private static final Vector STRIKE_FLY_VECTOR = new Vector(0, 1, 0);

    /**
     * 眩晕
     * @param plugin 插件
     * @param player 玩家
     * @param time 时间（秒）
     * @param force 是否无视免控
     */
    public static void stun(@NotNull AbstractPlugin plugin, @NotNull Player player, double time, boolean force) {
        UUID uuid = player.getUniqueId();
        STUN_TIMER.run(plugin, uuid, false, true, (int) Math.round(time * 1000L / (double) 50), 1,
            () -> force || !isSteadying(uuid),
            () -> {
                if (force || !isSteadying(uuid)) {
                    player.setWalkSpeed(0.0F);
                    return true;
                }
                return false;
            }, restTime -> player.setRotation(0.0F, 90.0F),
            () -> {
                player.setWalkSpeed(0.2F);
                return true;
            }
        );
    }

    /**
     * 清除眩晕
     * @param uuid UUID
     */
    public static void clearStun(@NotNull UUID uuid) {
        STUN_TIMER.clear(uuid);
    }

    /**
     * 判断是否眩晕
     * @param uuid UUID
     * @return 是否眩晕
     */
    public static boolean isStunning(@NotNull UUID uuid) {
        return STUN_TIMER.isRunning(uuid);
    }

    /**
     * 禁锢
     * @param plugin 插件
     * @param player 玩家
     * @param time 时间（秒）
     * @param force 是否无视免控
     */
    public static void root(@NotNull AbstractPlugin plugin, @NotNull Player player, double time, boolean force) {
        UUID uuid = player.getUniqueId();
        ROOT_TIMER.run(plugin, uuid, false, true, 1, (int) Math.round(time * 20L),
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

    /**
     * 清除禁锢
     * @param uuid UUID
     */
    public static void clearRoot(@NotNull UUID uuid) {
        ROOT_TIMER.clear(uuid);
    }

    /**
     * 判断是否禁锢
     * @param uuid UUID
     * @return 是否禁锢
     */
    public static boolean isRooting(@NotNull UUID uuid) {
        return ROOT_TIMER.isRunning(uuid);
    }

    /**
     * 沉默
     * @param player 玩家
     * @param time 时间（秒）
     */
    public static void silence(@NotNull Player player, double time) {
        SILENCE_TIMER.run(player, time, true, null, null, null);
    }

    /**
     * 清除沉默
     * @param uuid UUID
     */
    public static void clearSilence(@NotNull UUID uuid) {
        SILENCE_TIMER.clear(uuid);
    }

    /**
     * 判断是否沉默
     * @param uuid UUID
     * @return 是否沉默
     */
    public static boolean isSilencing(@NotNull UUID uuid) {
        return SILENCE_TIMER.isRunning(uuid);
    }

    /**
     * 霸体
     * @param player 玩家
     * @param time 时间（秒）
     */
    public static void steady(@NotNull Player player, double time) {
        STEADY_TIMER.run(player, time, true, null, null, null);
    }

    /**
     * 清除霸体
     * @param uuid UUID
     */
    public static void clearSteady(@NotNull UUID uuid) {
        STEADY_TIMER.clear(uuid);
    }

    /**
     * 获取是否霸体
     * @param uuid UUID
     * @return 是否霸体
     */
    public static boolean isSteadying(@NotNull UUID uuid) {
        return STEADY_TIMER.isRunning(uuid);
    }

    /**
     * 无敌
     * @param player 玩家
     * @param time 时间（秒）
     */
    public static void invincible(@NotNull Player player, double time) {
        INVINCIBLE_TIMER.run(player, time, true, null, null, null);
    }

    /**
     * 清除无敌
     * @param uuid UUID
     */
    public static void clearInvincible(@NotNull UUID uuid) {
        INVINCIBLE_TIMER.clear(uuid);
    }

    /**
     * 获取是否无敌
     * @param uuid UUID
     * @return 是否无敌
     */
    public static boolean isInvincibling(@NotNull UUID uuid) {
        return INVINCIBLE_TIMER.isRunning(uuid);
    }

    /**
     * 僵直
     * @param plugin 插件
     * @param player 玩家
     * @param time 时间（秒）
     */
    public static void stiff(@NotNull AbstractPlugin plugin, @NotNull Player player, double time) {
        UUID uuid = player.getUniqueId();
        STIFF_TIMER.run(plugin, uuid, false, true, 1, (int) Math.round(time * 20), null,
            () -> {
                player.setWalkSpeed(0.0F);
                return true;
            }, null,
            () -> {
                player.setWalkSpeed(0.2F);
                return true;
            }
        );
    }

    /**
     * 击飞
     * @param livingEntity 活体
     * @param velocity 速度
     */
    public static void strikeFly(@NotNull LivingEntity livingEntity, double velocity) {
        livingEntity.setVelocity(STRIKE_FLY_VECTOR.clone().multiply(velocity));
    }
}
