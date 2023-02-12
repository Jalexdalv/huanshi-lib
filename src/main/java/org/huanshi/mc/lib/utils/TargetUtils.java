package org.huanshi.mc.lib.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class TargetUtils {
    public static @NotNull Location correctLocation(@NotNull Location location, double x, double y, double z) {
        double radians = Math.toRadians(location.getYaw()), sin = Math.sin(radians), cos = Math.cos(radians);
        return location.clone().add(-x * cos - z * sin, y, z * cos - x * sin);
    }

    public static @NotNull Vector correctDirection(@NotNull Location location, double distance) {
        location.setPitch(0.0F);
        return location.getDirection().multiply(distance);
    }

    public static @NotNull BoundingBox getBoundingBox(@NotNull Location location, double x1, double y1, double z1, double x2, double y2, double z2) {
        return BoundingBox.of(correctLocation(location, x1, y1, z1), correctLocation(location, x2, y2, z2));
    }

    public static @NotNull Collection<Entity> getAABBEntities(@NotNull Location location, double x1, double y1, double z1, double x2, double y2, double z2) {
        return location.getWorld().getNearbyEntities(getBoundingBox(location, x1, y1, z1, x2, y2, z2));
    }
}
