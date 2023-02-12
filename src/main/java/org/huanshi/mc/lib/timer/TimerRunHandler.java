package org.huanshi.mc.lib.timer;

/**
 * 计时器运行时处理
 * @author: Jalexdalv
 */
public interface TimerRunHandler {
    /**
     * 处理
     * @param remainDuration 剩余时长（毫秒）
     */
    void handle(long remainDuration);
}
