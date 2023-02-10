package org.huanshi.mc.lib.utils;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.huanshi.mc.lib.timer.CdTimer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * 状态工具类
 * @author Jalexdalv
 */
public class StatusUtils {
    private static final CdTimer STUN_TIMER = new CdTimer(), ROOT_TIMER = new CdTimer(), SILENCE_TIMER = new CdTimer(), STEADY_TIMER = new CdTimer(), INVINCIBLE_TIMER = new CdTimer(), STIFF_TIMER = new CdTimer();
    private static final Vector STRIKE_FLY_VECTOR = new Vector(0, 1, 0);

    /**
     * 眩晕
     * @param player 玩家
     * @param time 时间（秒）
     * @param force 是否无视免控
     */
    public static void stun(@NotNull Player player, double time, boolean force) {
        STUN_TIMER.run(player, time, true, () -> force || isSteadying(player.getUniqueId()), () -> force || isSteadying(player.getUniqueId()), null);
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
     * @param player 玩家
     * @param time 时间（秒）
     * @param force 是否无视免控
     */
    public static void root(@NotNull Player player, double time, boolean force) {
        ROOT_TIMER.run(player, time, true, () -> force || isSteadying(player.getUniqueId()), () -> force || isSteadying(player.getUniqueId()), null);
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
     * @param force 是否无视免控
     */
    public static void silence(@NotNull Player player, double time, boolean force) {
        SILENCE_TIMER.run(player, time, true, () -> force || isSteadying(player.getUniqueId()), () -> force || isSteadying(player.getUniqueId()), null);
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
     * @param player 玩家
     * @param time 时间（秒）
     */
    public static void stiff(@NotNull Player player, double time) {
        STIFF_TIMER.run(player, time, true, null, null, null);
    }

    /**
     * 获取是否僵直
     * @param uuid UUID
     * @return 是否僵直
     */
    public static boolean isStiffing(@NotNull UUID uuid) {
        return STIFF_TIMER.isRunning(uuid);
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
