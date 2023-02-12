package org.huanshi.mc.lib.timer;

import org.bukkit.entity.Player;
import org.huanshi.mc.lib.lang.Zh;
import org.huanshi.mc.lib.utils.TimerUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * CD计时器
 * @author: Jalexdalv
 */
public class CdTimer {
    private final Map<UUID, Long> finishTimeMap = new HashMap<>();

    /**
     * 启动
     * @param player 玩家
     * @param duration 时长（毫秒）
     * @param reentry 是否重入
     * @param timerReentryHandler 计时器重入时处理
     * @param timerStartHandler 计时器启动时处理
     * @param timerRunHandler 计时器运行时处理
     */
    public void run(@NotNull Player player, long duration, boolean reentry, @Nullable TimerReentryHandler timerReentryHandler, @Nullable TimerStartHandler timerStartHandler, @Nullable TimerRunHandler timerRunHandler) {
        UUID uuid = player.getUniqueId();
        long remainDuration = Math.max(finishTimeMap.getOrDefault(uuid, 0L) - System.currentTimeMillis(), 0);
        if (remainDuration > 0) {
            if (reentry) {
                if (timerReentryHandler == null || timerReentryHandler.handle()) {
                    finishTimeMap.merge(uuid, System.currentTimeMillis() + duration, Math::max);
                }
            } else {
                if (timerRunHandler != null) {
                    timerRunHandler.handle(remainDuration);
                }
                player.sendActionBar(Zh.cd(TimerUtils.convertMillisecondToSecond(remainDuration)));
            }
        } else if (timerStartHandler == null || timerStartHandler.handle()) {
            finishTimeMap.put(uuid, System.currentTimeMillis() + duration);
        }
    }

    /**
     * 清除
     * @param uuid UUID
     */
    public void clear(@NotNull UUID uuid) {
        finishTimeMap.remove(uuid);
    }

    /**
     * 获取正在运行的集合
     * @return UUID
     */
    public @NotNull Set<UUID> getRunnings() {
        Set<UUID> runningSet = new HashSet<>();
        for (Map.Entry<UUID, Long> entry : finishTimeMap.entrySet()) {
            if (entry.getValue() - System.currentTimeMillis() > 0) {
                runningSet.add(entry.getKey());
            }
        }
        return runningSet;
    }

    /**
     * 判断是否正在运行
     * @param uuid UUID
     * @return 是否正在运行
     */
    public boolean isRunning(@NotNull UUID uuid) {
        return finishTimeMap.getOrDefault(uuid, 0L) - System.currentTimeMillis() > 0;
    }
}
