package org.huanshi.mc.lib.timer;

/**
 * 计时器运行时处理
 * @author: Jalexdalv
 */
public interface TimerRunHandler {
    /**
     * 处理
     * @param restTime 剩余时间
     */
    void handle(int restTime);
}
