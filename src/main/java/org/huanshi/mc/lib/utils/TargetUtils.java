package org.huanshi.mc.lib.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class TargetUtils {
    public static @NotNull Location getFixLocation(@NotNull Location location, double x, double z) {
        double radians = Math.toRadians(location.getYaw());
        double sin = Math.sin(radians), cos = Math.cos(radians);
        return location.clone().add(-x * cos - z * sin, 0, z * cos - x * sin);
    }

    public static @NotNull BoundingBox getAABB(@NotNull Location location, double x1, double y1, double z1, double x2, double y2, double z2) {
        return BoundingBox.of(getFixLocation(location, x1, z1).add(0, y1, 0), getFixLocation(location, x2, z2).add(0, y2, 0));
    }

    public static @NotNull Collection<Entity> getAABBEntities(@NotNull Location location, double x1, double y1, double z1, double x2, double y2, double z2) {
        return location.getWorld().getNearbyEntities(getAABB(location, x1, y1, z1, x2, y2, z2));
    }
}
