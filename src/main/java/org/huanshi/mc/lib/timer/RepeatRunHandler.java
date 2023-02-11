package org.huanshi.mc.lib.timer;

/**
 * 重复运行时处理
 * @author: Jalexdalv
 */
public interface RepeatRunHandler {
    /**
     * 处理
     * @param restTimes 剩余次数
     */
    boolean handle(int restTimes);
}
