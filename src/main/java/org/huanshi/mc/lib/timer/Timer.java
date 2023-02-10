package org.huanshi.mc.lib.timer;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.lib.AbstractPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 计时器
 * @author: Jalexdalv
 */
public class Timer {
    private final Map<UUID, Integer> restTimeMap = new ConcurrentHashMap<>();

    /**
     * 启动
     * @param plugin 插件
     * @param uuid UUID
     * @param async 是否异步
     * @param reentry 是否重入
     * @param repeat 重复次数
     * @param period 触发间隔（tick）
     * @param timerReentryHandler 计时器重入时处理
     * @param timerStartHandler 计时器启动时处理
     * @param timerRunHandler 计时器运行时处理
     * @param timerFinishHandler 计时器结束时处理
     */
    public void run(@NotNull AbstractPlugin plugin, @NotNull UUID uuid, boolean async, boolean reentry, int repeat, int period, @Nullable TimerReentryHandler timerReentryHandler, @Nullable TimerStartHandler timerStartHandler, @Nullable TimerRunHandler timerRunHandler, @Nullable TimerFinishHandler timerFinishHandler) {
        if (isRunning(uuid)) {
            if (reentry && (timerReentryHandler == null || timerReentryHandler.handle())) {
                restTimeMap.merge(uuid, repeat, Integer::max);
            }
        } else if (timerStartHandler == null || timerStartHandler.handle()) {
            restTimeMap.put(uuid, repeat);
            BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                @Override
                public void run() {
                    Integer restTime = restTimeMap.getOrDefault(uuid, 0);
                    if (restTime > 0) {
                        if (timerRunHandler != null) {
                            timerRunHandler.handle(restTime);
                        }
                        restTimeMap.put(uuid, restTime - 1);
                    } else if (timerFinishHandler == null || timerFinishHandler.handle()) {
                        restTimeMap.remove(uuid);
                        cancel();
                    }
                }
            };
            if (async) {
                bukkitRunnable.runTaskTimerAsynchronously(plugin, 0L, period);
            } else {
                bukkitRunnable.runTaskTimer(plugin, 0L, period);
            }
        }
    }

    /**
     * 清除
     * @param uuid UUID
     */
    public void clear(@NotNull UUID uuid) {
        restTimeMap.remove(uuid);
    }

    /**
     * 获取正在运行的集合
     * @return UUID
     */
    public @NotNull Set<UUID> getRunnings() {
        Set<UUID> runningSet = new HashSet<>();
        for (Map.Entry<UUID, Integer> entry : restTimeMap.entrySet()) {
            if (entry.getValue() > 0) {
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
        return restTimeMap.getOrDefault(uuid, 0) > 0;
    }
}
