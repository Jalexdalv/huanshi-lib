package org.huanshi.mc.lib.timer;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.lib.AbstractPlugin;
import org.huanshi.mc.lib.utils.TimerUtils;
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
    private final Map<UUID, Integer> remainRepeatMap = new ConcurrentHashMap<>();

    /**
     * 启动
     * @param plugin 插件
     * @param uuid UUID
     * @param async 是否异步
     * @param reentry 是否重入
     * @param duration 时长（毫秒）
     * @param delay 延迟（tick）
     * @param period 间隔（tick）
     * @param timerReentryHandler 计时器重入时处理
     * @param timerStartHandler 计时器启动时处理
     * @param timerRunHandler 计时器运行时处理
     * @param timerFinishHandler 计时器结束时处理
     */
    public void run(@NotNull AbstractPlugin plugin, @NotNull UUID uuid, boolean async, boolean reentry, long duration, int delay, int period, @Nullable TimerReentryHandler timerReentryHandler, @Nullable TimerStartHandler timerStartHandler, @Nullable TimerRunHandler timerRunHandler, @Nullable TimerFinishHandler timerFinishHandler) {
        if (isRunning(uuid)) {
            if (reentry && (timerReentryHandler == null || timerReentryHandler.handle())) {
                remainRepeatMap.merge(uuid, TimerUtils.convertMillisecondToRepeat(duration, period), Integer::max);
            }
        } else if (timerStartHandler == null || timerStartHandler.handle()) {
            remainRepeatMap.put(uuid, TimerUtils.convertMillisecondToRepeat(duration, period));
            BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                @Override
                public void run() {
                    Integer remainRepeat = remainRepeatMap.getOrDefault(uuid, 0);
                    if (remainRepeat > 0) {
                        remainRepeatMap.put(uuid, remainRepeat - 1);
                        if (timerRunHandler != null) {
                            timerRunHandler.handle(TimerUtils.convertRepeatToMillisecond(remainRepeat, period));
                        }
                    } else if (timerFinishHandler == null || timerFinishHandler.handle()) {
                        remainRepeatMap.remove(uuid);
                        cancel();
                    }
                }
            };
            if (async) {
                bukkitRunnable.runTaskTimerAsynchronously(plugin, delay, period);
            } else {
                bukkitRunnable.runTaskTimer(plugin, delay, period);
            }
        }
    }

    /**
     * 清除
     * @param uuid UUID
     */
    public void clear(@NotNull UUID uuid) {
        remainRepeatMap.remove(uuid);
    }

    /**
     * 获取正在运行的集合
     * @return UUID
     */
    public @NotNull Set<UUID> getRunnings() {
        Set<UUID> runningSet = new HashSet<>();
        for (Map.Entry<UUID, Integer> entry : remainRepeatMap.entrySet()) {
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
        return remainRepeatMap.getOrDefault(uuid, 0) > 0;
    }
}
