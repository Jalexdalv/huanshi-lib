package org.huanshi.mc.lib.utils;

import org.bukkit.scheduler.BukkitRunnable;
import org.huanshi.mc.lib.AbstractPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

public class TimerUtils {
    /**
     * 重复
     * @param plugin 插件
     * @param async 是否异步
     * @param repeat 重复次数
     * @param delay 延迟（tick）
     * @param period 间隔（tick）
     * @param repeatStartHandler 重复启动时处理
     * @param repeatRunHandler 重复运行时处理
     * @param repeatFinishHandler 重复结束时处理
     */
    public static void repeat(@NotNull AbstractPlugin plugin, boolean async, int repeat, int delay, int period, @Nullable RepeatStartHandler repeatStartHandler, @Nullable RepeatRunHandler repeatRunHandler, @Nullable RepeatFinishHandler repeatFinishHandler) {
        AtomicInteger atomicInteger = new AtomicInteger(repeat);
        if (repeatStartHandler == null || repeatStartHandler.handle()) {
            BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if ((atomicInteger.getAndDecrement() <= 0 || (repeatRunHandler != null && !repeatRunHandler.handle(atomicInteger.get()))) && (repeatFinishHandler == null || repeatFinishHandler.handle())) {
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
     * 毫秒转换为tick
     * @param millisecond 毫秒
     * @return tick
     */
    public static int convertMillisecondToTick(long millisecond) {
        return (int) Math.ceil((double) millisecond / (double) 50);
    }

    /**
     * 毫秒转换为重复次数
     * @param second 毫秒
     * @param period 间隔（tick）
     * @return 重复次数
     */
    public static int convertMillisecondToRepeat(long millisecond, int period) {
        return (int) Math.ceil((double) millisecond / (double) 50 / (double) period);
    }

    /**
     * 重复次数转换为毫秒
     * @param repeat 重复次数
     * @param period 间隔（tick）
     * @return 毫秒
     */
    public static long convertRepeatToMillisecond(int repeat, int period) {
        return (long) repeat * period * 50;
    }

    /**
     * 毫秒转换为整数秒
     * @param millisecond 毫秒
     * @return 整数秒
     */
    public static int convertMillisecondToSecond(long millisecond) {
        return (int) Math.ceil((double) millisecond / (double) 1000);
    }
}
