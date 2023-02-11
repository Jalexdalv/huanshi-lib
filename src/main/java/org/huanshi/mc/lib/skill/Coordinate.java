package org.huanshi.mc.lib.skill;

import org.jetbrains.annotations.NotNull;

/**
 * 坐标轴
 * @author Jalexdalv
 */
public enum Coordinate {
    XY("xy"), YZ("yz"), XZ("xz");

    final String name;

    /**
     * 构造函数
     * @param name 名称
     */
    Coordinate(String name) {
        this.name = name;
    }

    /**
     * 获取名称
     * @return 名称
     */
    public @NotNull String getName() {
        return name;
    }
}
