package org.huanshi.mc.lib.utils;

/**
 * 重复运行时处理
 * @author: Jalexdalv
 */
public interface RepeatRunHandler {
    /**
     * 处理
     * @param remainRepeat 剩余重复次数
     */
    boolean handle(int remainRepeat);
}
