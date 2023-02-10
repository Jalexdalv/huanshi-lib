package org.huanshi.mc.lib.timer;

/**
 * 计时器结束时处理
 * @author: Jalexdalv
 */
public interface TimerFinishHandler {
    /**
     * 处理
     * @return 是否执行
     */
    boolean handle();
}
