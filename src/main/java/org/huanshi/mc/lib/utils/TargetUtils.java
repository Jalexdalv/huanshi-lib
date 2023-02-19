package org.huanshi.mc.lib.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Predicate;

public class TargetUtils {
    public interface AimHandler<T extends LivingEntity> {
        void handle(@NotNull T t);
    }

    public static @NotNull Location correctLocation(@NotNull Location location, double x, double y, double z) {
        double radians = Math.toRadians(location.getYaw()), sin = Math.sin(radians), cos = Math.cos(radians);
        return location.clone().add(- x * cos - z * sin, y, z * cos - x * sin);
    }

    public static @NotNull Vector correctDirection(@NotNull Location location, double distance) {
        location.setPitch(0.0F);
        return location.getDirection().multiply(distance);
    }

    public static @Nullable LivingEntity getLivingEntity(@NotNull LivingEntity livingEntity, int distance, @Nullable Predicate<LivingEntity> predicate) {
        Entity entity = livingEntity.getTargetEntity(distance);
        if (entity instanceof LivingEntity targetLivingEntity && (predicate == null || predicate.test(targetLivingEntity))) {
            return targetLivingEntity;
        }
        return null;
    }

    public static boolean aimLivingEntity(@NotNull LivingEntity livingEntity, int distance, @Nullable Predicate<LivingEntity> predicate, @NotNull AimHandler<LivingEntity> aimHandler) {
        LivingEntity targetLivingEntity = getLivingEntity(livingEntity, distance, predicate);
        if (targetLivingEntity != null) {
            aimHandler.handle(targetLivingEntity);
            return true;
        }
        return false;
    }

    public static boolean aimEnemy(@NotNull LivingEntity livingEntity, int distance, @Nullable Predicate<LivingEntity> predicate, @NotNull AimHandler<LivingEntity> aimHandler) {
        LivingEntity targetLivingEntity = getLivingEntity(livingEntity, distance, predicate);
        if (targetLivingEntity != null && targetLivingEntity != livingEntity && (!(targetLivingEntity instanceof Tameable tameable) || tameable.getOwner() != livingEntity)) {
            aimHandler.handle(targetLivingEntity);
            return true;
        }
        return false;
    }

    public static @Nullable Player getPlayer(@NotNull LivingEntity livingEntity, int distance, @Nullable Predicate<Player> predicate) {
        Entity entity = livingEntity.getTargetEntity(distance);
        if (entity instanceof Player player && (predicate == null || predicate.test(player))) {
            return player;
        }
        return null;
    }

    public static void aimPlayer(@NotNull LivingEntity livingEntity, int distance, @Nullable Predicate<Player> predicate, @NotNull AimHandler<Player> aimHandler) {
        Player player = getPlayer(livingEntity, distance, predicate);
        if (player != null) {
            aimHandler.handle(player);
        }
    }

    public static @NotNull Collection<Entity> getLivingEntities(@NotNull Location location, double x1, double y1, double z1, double x2, double y2, double z2, @Nullable Predicate<LivingEntity> predicate) {
        double radians = Math.toRadians(location.getYaw()), sin = Math.sin(radians), cos = Math.cos(radians);
        double correctX1 = location.getX() + x1 * cos - z1 * sin, correctZ1 = location.getZ() + z1 * cos + x1 * sin, correctX2 = location.getX() - x2 * cos - z2 * sin, correctZ2 = location.getZ() + z2 * cos - x2 * sin, correctX3 = location.getX() + x2 * cos - z2 * sin, correctZ3 = location.getZ() + z2 * cos + x2 * sin, correctX4 = location.getX() - x1 * cos - z1 * sin, correctZ4 = location.getZ() + z1 * cos - x1 * sin;
        double subtractX12 = correctX2 - correctX1, subtractZ12 = correctZ2 - correctZ1, subtractX34 = correctX4 - correctX3, subtractZ34 = correctZ4 - correctZ3, subtractX23 = correctX3 - correctX2, subtractZ23 = correctZ3 - correctZ2, subtractX41 = correctX1 - correctX4, subtractZ41 = correctZ1 - correctZ4;
        return location.getWorld().getNearbyEntities(BoundingBox.of(location.clone().set(Math.max(Math.max(correctX1, correctX2), Math.max(correctX3, correctX4)), location.getY() + Math.max(y1, y2), Math.max(Math.max(correctZ1, correctZ2), Math.max(correctZ3, correctZ4))), location.clone().set(Math.min(Math.min(correctX1, correctX2), Math.min(correctX3, correctX4)), location.getY() + Math.min(y1, y2), Math.min(Math.min(correctZ1, correctZ2), Math.min(correctZ3, correctZ4)))), entity -> {
            Location targetLocation = entity.getLocation();
            double x = targetLocation.getX();
            double z = targetLocation.getZ();
            return entity instanceof LivingEntity livingEntity && (predicate == null || predicate.test(livingEntity)) && (subtractX12 * (z - correctZ1) - (x - correctX1) * subtractZ12) * (subtractX34 * (z - correctZ3) - (x - correctX3) * subtractZ34) >= 0 && (subtractX23 * (z - correctZ2) - (x - correctX2) * subtractZ23) * (subtractX41 * (z - correctZ4) - (x - correctX4) * subtractZ41) >= 0;
        });
    }

    public static void aimLivingEntities(@NotNull Location location, double x1, double y1, double z1, double x2, double y2, double z2, @Nullable Predicate<LivingEntity> predicate, @NotNull AimHandler<LivingEntity> aimHandler) {
        for (Entity entity : getLivingEntities(location, x1, y1, z1, x2, y2, z2, predicate)) {
            aimHandler.handle((LivingEntity) entity);
        }
    }

    public static void aimEnemies(@NotNull LivingEntity livingEntity, @NotNull Location location, double x1, double y1, double z1, double x2, double y2, double z2, @Nullable Predicate<LivingEntity> predicate, @NotNull AimHandler<LivingEntity> aimHandler) {
        for (Entity entity : getLivingEntities(location, x1, y1, z1, x2, y2, z2, predicate)) {
            if (entity != livingEntity && (!(entity instanceof Tameable tameable) || tameable.getOwner() != livingEntity)) {
                aimHandler.handle((LivingEntity) entity);
            }
        }
    }

    public static @NotNull Collection<Entity> getPlayers(@NotNull Location location, double x1, double y1, double z1, double x2, double y2, double z2, @Nullable Predicate<Player> predicate) {
        double radians = Math.toRadians(location.getYaw()), sin = Math.sin(radians), cos = Math.cos(radians);
        double correctX1 = location.getX() + x1 * cos - z1 * sin, correctZ1 = location.getZ() + z1 * cos + x1 * sin, correctX2 = location.getX() - x2 * cos - z2 * sin, correctZ2 = location.getZ() + z2 * cos - x2 * sin, correctX3 = location.getX() + x2 * cos - z2 * sin, correctZ3 = location.getZ() + z2 * cos + x2 * sin, correctX4 = location.getX() - x1 * cos - z1 * sin, correctZ4 = location.getZ() + z1 * cos - x1 * sin;
        double subtractX12 = correctX2 - correctX1, subtractZ12 = correctZ2 - correctZ1, subtractX34 = correctX4 - correctX3, subtractZ34 = correctZ4 - correctZ3, subtractX23 = correctX3 - correctX2, subtractZ23 = correctZ3 - correctZ2, subtractX41 = correctX1 - correctX4, subtractZ41 = correctZ1 - correctZ4;
        return location.getWorld().getNearbyEntities(BoundingBox.of(location.clone().set(Math.max(Math.max(correctX1, correctX2), Math.max(correctX3, correctX4)), location.getY() + Math.max(y1, y2), Math.max(Math.max(correctZ1, correctZ2), Math.max(correctZ3, correctZ4))), location.clone().set(Math.min(Math.min(correctX1, correctX2), Math.min(correctX3, correctX4)), location.getY() + Math.min(y1, y2), Math.min(Math.min(correctZ1, correctZ2), Math.min(correctZ3, correctZ4)))), entity -> {
            Location targetLocation = entity.getLocation();
            double x = targetLocation.getX();
            double z = targetLocation.getZ();
            return entity instanceof Player player && (predicate == null || predicate.test(player)) && (subtractX12 * (z - correctZ1) - (x - correctX1) * subtractZ12) * (subtractX34 * (z - correctZ3) - (x - correctX3) * subtractZ34) >= 0 && (subtractX23 * (z - correctZ2) - (x - correctX2) * subtractZ23) * (subtractX41 * (z - correctZ4) - (x - correctX4) * subtractZ41) >= 0;
        });
    }

    public static void aimPlayers(@NotNull Location location, double x1, double y1, double z1, double x2, double y2, double z2, @Nullable Predicate<Player> predicate, @NotNull AimHandler<Player> aimHandler) {
        for (Entity entity : getPlayers(location, x1, y1, z1, x2, y2, z2, predicate)) {
            aimHandler.handle((Player) entity);
        }
    }
}
