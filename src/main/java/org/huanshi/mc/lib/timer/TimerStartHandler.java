package org.huanshi.mc.lib.timer;

/**
 * 计时器启动时处理
 * @author: Jalexdalv
 */
public interface TimerStartHandler {
    /**
     * 处理
     * @return 是否执行
     */
    boolean handle();
}
