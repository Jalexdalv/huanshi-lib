package org.huanshi.mc.lib.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class TargetUtils {
    /**
     * 修正位置
     * @param location 位置
     * @param x x轴相对坐标
     * @param y y轴相对坐标
     * @param z z轴相对坐标
     * @return 位置
     */
    public static @NotNull Location correctLocation(@NotNull Location location, double x, double y, double z) {
        double radians = Math.toRadians(location.getYaw()), sin = Math.sin(radians), cos = Math.cos(radians);
        return location.clone().add(-x * cos - z * sin, y, z * cos - x * sin);
    }

    /**
     * 修正方向
     * @param location 位置
     * @param distance 距离
     * @return 方向
     */
    public static @NotNull Vector correctDirection(@NotNull Location location, double distance) {
        location.setPitch(0.0F);
        return location.getDirection().multiply(distance);
    }

    /**
     * 获取长方体区域
     * @param location 位置
     * @param x1 第一个点的x轴相对坐标
     * @param y1 第一个点的y轴相对坐标
     * @param z1 第一个点的z轴相对坐标
     * @param x2 第二个点的x轴相对坐标
     * @param y2 第二个点的y轴相对坐标
     * @param z2 第二个点的z轴相对坐标
     * @return 长方体区域
     */
    public static @NotNull BoundingBox getBoundingBox(@NotNull Location location, double x1, double y1, double z1, double x2, double y2, double z2) {
        return BoundingBox.of(correctLocation(location, x1, y1, z1), correctLocation(location, x2, y2, z2));
    }

    /**
     * 获取长方体区域内的实体集合
     * @param location 位置
     * @param x1 第一个点的x轴相对坐标
     * @param y1 第一个点的y轴相对坐标
     * @param z1 第一个点的z轴相对坐标
     * @param x2 第二个点的x轴相对坐标
     * @param y2 第二个点的y轴相对坐标
     * @param z2 第二个点的z轴相对坐标
     * @return 长方体区域内的实体集合
     */
    public static @NotNull Collection<Entity> getAABBEntities(@NotNull Location location, double x1, double y1, double z1, double x2, double y2, double z2) {
        return location.getWorld().getNearbyEntities(getBoundingBox(location, x1, y1, z1, x2, y2, z2));
    }
}
