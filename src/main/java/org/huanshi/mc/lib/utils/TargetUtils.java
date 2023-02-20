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
    public static @NotNull Location correctLocation(@NotNull final Location location, final double x, final double y, final double z) {
        final double radians = Math.toRadians(location.getYaw()), sin = Math.sin(radians), cos = Math.cos(radians);
        return location.clone().add(- x * cos - z * sin, y, z * cos - x * sin);
    }

    public static @NotNull Vector correctDirection(@NotNull final Location location, final double distance) {
        location.setPitch(0.0F);
        return location.getDirection().multiply(distance);
    }

    public static @Nullable LivingEntity getLivingEntity(@NotNull final LivingEntity livingEntity, final int distance, @Nullable final Predicate<LivingEntity> predicate) {
        final Entity entity = livingEntity.getTargetEntity(distance);
        if (entity instanceof final LivingEntity targetLivingEntity && (predicate == null || predicate.test(targetLivingEntity))) {
            return targetLivingEntity;
        }
        return null;
    }

    public static boolean aimLivingEntity(@NotNull final LivingEntity livingEntity, final int distance, @Nullable final Predicate<LivingEntity> predicate, @NotNull final AimHandler<LivingEntity> aimHandler) {
        final LivingEntity targetLivingEntity = getLivingEntity(livingEntity, distance, predicate);
        if (targetLivingEntity != null) {
            aimHandler.handle(targetLivingEntity);
            return true;
        }
        return false;
    }

    public static boolean aimEnemy(@NotNull final LivingEntity livingEntity, final int distance, @Nullable final Predicate<LivingEntity> predicate, @NotNull final AimHandler<LivingEntity> aimHandler) {
        final LivingEntity targetLivingEntity = getLivingEntity(livingEntity, distance, predicate);
        if (targetLivingEntity != null && targetLivingEntity != livingEntity && (!(targetLivingEntity instanceof final Tameable tameable) || tameable.getOwner() != livingEntity)) {
            aimHandler.handle(targetLivingEntity);
            return true;
        }
        return false;
    }

    public static @Nullable Player getPlayer(@NotNull final LivingEntity livingEntity, final int distance, @Nullable final Predicate<Player> predicate) {
        final Entity entity = livingEntity.getTargetEntity(distance);
        if (entity instanceof final Player player && (predicate == null || predicate.test(player))) {
            return player;
        }
        return null;
    }

    public static void aimPlayer(@NotNull final LivingEntity livingEntity, final int distance, @Nullable final Predicate<Player> predicate, @NotNull final AimHandler<Player> aimHandler) {
        final Player player = getPlayer(livingEntity, distance, predicate);
        if (player != null) {
            aimHandler.handle(player);
        }
    }

    public static @NotNull Collection<Entity> getLivingEntities(@NotNull final Location location, final double x1, final double y1, final double z1, final double x2, final double y2, final double z2, @Nullable final Predicate<LivingEntity> predicate) {
        final double radians = Math.toRadians(location.getYaw()), sin = Math.sin(radians), cos = Math.cos(radians);
        final double correctX1 = location.getX() + x1 * cos - z1 * sin, correctZ1 = location.getZ() + z1 * cos + x1 * sin, correctX2 = location.getX() - x2 * cos - z2 * sin, correctZ2 = location.getZ() + z2 * cos - x2 * sin, correctX3 = location.getX() + x2 * cos - z2 * sin, correctZ3 = location.getZ() + z2 * cos + x2 * sin, correctX4 = location.getX() - x1 * cos - z1 * sin, correctZ4 = location.getZ() + z1 * cos - x1 * sin;
        final double subtractX12 = correctX2 - correctX1, subtractZ12 = correctZ2 - correctZ1, subtractX34 = correctX4 - correctX3, subtractZ34 = correctZ4 - correctZ3, subtractX23 = correctX3 - correctX2, subtractZ23 = correctZ3 - correctZ2, subtractX41 = correctX1 - correctX4, subtractZ41 = correctZ1 - correctZ4;
        return location.getWorld().getNearbyEntities(BoundingBox.of(location.clone().set(Math.max(Math.max(correctX1, correctX2), Math.max(correctX3, correctX4)), location.getY() + Math.max(y1, y2), Math.max(Math.max(correctZ1, correctZ2), Math.max(correctZ3, correctZ4))), location.clone().set(Math.min(Math.min(correctX1, correctX2), Math.min(correctX3, correctX4)), location.getY() + Math.min(y1, y2), Math.min(Math.min(correctZ1, correctZ2), Math.min(correctZ3, correctZ4)))), entity -> {
            final Location targetLocation = entity.getLocation();
            final double x = targetLocation.getX(), z = targetLocation.getZ();
            return entity instanceof final LivingEntity livingEntity && (predicate == null || predicate.test(livingEntity)) && (subtractX12 * (z - correctZ1) - (x - correctX1) * subtractZ12) * (subtractX34 * (z - correctZ3) - (x - correctX3) * subtractZ34) >= 0 && (subtractX23 * (z - correctZ2) - (x - correctX2) * subtractZ23) * (subtractX41 * (z - correctZ4) - (x - correctX4) * subtractZ41) >= 0;
        });
    }

    public static void aimLivingEntities(@NotNull final Location location, final double x1, final double y1, final double z1, final double x2, final double y2, final double z2, @Nullable final Predicate<LivingEntity> predicate, @NotNull final AimHandler<LivingEntity> aimHandler) {
        for (final Entity entity : getLivingEntities(location, x1, y1, z1, x2, y2, z2, predicate)) {
            aimHandler.handle((LivingEntity) entity);
        }
    }

    public static void aimEnemies(@NotNull final LivingEntity livingEntity, @NotNull final Location location, final double x1, final double y1, final double z1, final double x2, final double y2, final double z2, @Nullable final Predicate<LivingEntity> predicate, @NotNull final AimHandler<LivingEntity> aimHandler) {
        for (final Entity entity : getLivingEntities(location, x1, y1, z1, x2, y2, z2, predicate)) {
            if (entity != livingEntity && (!(entity instanceof final Tameable tameable) || tameable.getOwner() != livingEntity)) {
                aimHandler.handle((LivingEntity) entity);
            }
        }
    }

    public static @NotNull Collection<Entity> getPlayers(@NotNull final Location location, final double x1, final double y1, final double z1, final double x2, final double y2, final double z2, @Nullable final Predicate<Player> predicate) {
        final double radians = Math.toRadians(location.getYaw()), sin = Math.sin(radians), cos = Math.cos(radians);
        final double correctX1 = location.getX() + x1 * cos - z1 * sin, correctZ1 = location.getZ() + z1 * cos + x1 * sin, correctX2 = location.getX() - x2 * cos - z2 * sin, correctZ2 = location.getZ() + z2 * cos - x2 * sin, correctX3 = location.getX() + x2 * cos - z2 * sin, correctZ3 = location.getZ() + z2 * cos + x2 * sin, correctX4 = location.getX() - x1 * cos - z1 * sin, correctZ4 = location.getZ() + z1 * cos - x1 * sin;
        final double subtractX12 = correctX2 - correctX1, subtractZ12 = correctZ2 - correctZ1, subtractX34 = correctX4 - correctX3, subtractZ34 = correctZ4 - correctZ3, subtractX23 = correctX3 - correctX2, subtractZ23 = correctZ3 - correctZ2, subtractX41 = correctX1 - correctX4, subtractZ41 = correctZ1 - correctZ4;
        return location.getWorld().getNearbyEntities(BoundingBox.of(location.clone().set(Math.max(Math.max(correctX1, correctX2), Math.max(correctX3, correctX4)), location.getY() + Math.max(y1, y2), Math.max(Math.max(correctZ1, correctZ2), Math.max(correctZ3, correctZ4))), location.clone().set(Math.min(Math.min(correctX1, correctX2), Math.min(correctX3, correctX4)), location.getY() + Math.min(y1, y2), Math.min(Math.min(correctZ1, correctZ2), Math.min(correctZ3, correctZ4)))), entity -> {
            final Location targetLocation = entity.getLocation();
            final double x = targetLocation.getX(), z = targetLocation.getZ();
            return entity instanceof final Player player && (predicate == null || predicate.test(player)) && (subtractX12 * (z - correctZ1) - (x - correctX1) * subtractZ12) * (subtractX34 * (z - correctZ3) - (x - correctX3) * subtractZ34) >= 0 && (subtractX23 * (z - correctZ2) - (x - correctX2) * subtractZ23) * (subtractX41 * (z - correctZ4) - (x - correctX4) * subtractZ41) >= 0;
        });
    }

    public static void aimPlayers(@NotNull final Location location, final double x1, final double y1, final double z1, final double x2, final double y2, final double z2, @Nullable final Predicate<Player> predicate, @NotNull final AimHandler<Player> aimHandler) {
        for (final Entity entity : getPlayers(location, x1, y1, z1, x2, y2, z2, predicate)) {
            aimHandler.handle((Player) entity);
        }
    }
}
