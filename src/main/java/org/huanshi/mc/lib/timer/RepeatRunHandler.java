package org.huanshi.mc.lib.timer;

/**
 * 重复运行时处理
 * @author: Jalexdalv
 */
public interface RepeatRunHandler {
    /**
     * 处理
     * @param restTime 剩余时间（秒）
     */
    boolean handle(double restTime);
}
