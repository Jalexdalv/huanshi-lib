package org.huanshi.mc.lib.utils;

import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.lib.timer.Countdowner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ParticleUtils {
    public static <T> void play(@NotNull final Location location, @NotNull final Particle particle, final int count, final double offsetX, final double offsetY, final double offsetZ, final double speed, @Nullable final T data) {
        final double correctRadians = Math.toRadians(location.getYaw()), correctSin = Math.sin(correctRadians), correctCos = Math.cos(correctRadians);
        final double correctOffsetX = - offsetX * correctCos - offsetZ * correctSin, correctOffsetZ = offsetZ * correctCos - offsetX * correctSin;
        TargetUtils.aimPlayers(location, 20.0D, 10.0D, 20.0D, -20.0D, -10.0D, -20.0D, null, player -> player.spawnParticle(particle, location, count, correctOffsetX, offsetY, correctOffsetZ, speed, data));
    }

    public static <T> void play2D(@NotNull final Location location, @NotNull final Particle particle, @NotNull final Coordinate coordinate, final int count, final double offsetX, final double offsetY, final double offsetZ, final double speed, @Nullable final T data, final double startAngle, final double endAngle, final double radius, final int repeat) {
        final Collection<Entity> entityCollection = TargetUtils.getPlayers(location, 20.0D, 10.0D, 20.0D, -20.0D, -10.0D, -20.0D, null);
        final double correctRadians = Math.toRadians(location.getYaw()), correctSin = Math.sin(correctRadians), correctCos = Math.cos(correctRadians);
        final double correctOffsetX = - offsetX * correctCos - offsetZ * correctSin, correctOffsetZ = offsetZ * correctCos - offsetX * correctSin;
        final double stepAngle = (endAngle - startAngle) / (double) repeat;
        switch (coordinate) {
            case XY -> {
                for (double angle = startAngle; angle < endAngle; angle = angle + stepAngle) {
                    final double radians = Math.toRadians(angle), x = radius * Math.sin(radians), y = radius * Math.sin(radians);
                    final double correctX = - x * correctCos, correctZ = - x * correctSin;
                    location.add(correctX, y, correctZ);
                    for (final Entity entity : entityCollection) {
                        ((Player) entity).spawnParticle(particle, location, count, correctOffsetX, offsetY, correctOffsetZ, speed, data);
                    }
                    location.subtract(correctX, y, correctZ);
                }
            } case YZ -> {
                for (double angle = startAngle; angle < endAngle; angle = angle + stepAngle) {
                    final double radians = Math.toRadians(angle), z = radius * Math.cos(radians), y = radius * Math.sin(radians);
                    final double correctX = - z * correctSin, correctZ = z * correctCos;
                    location.add(correctX, y, correctZ);
                    for (final Entity entity : entityCollection) {
                        ((Player) entity).spawnParticle(particle, location, count, correctOffsetX, offsetY, correctOffsetZ, speed, data);
                    }
                    location.subtract(correctX, y, correctZ);
                }
            } case XZ -> {
                for (double angle = startAngle; angle < endAngle; angle = angle + stepAngle) {
                    final double radians = Math.toRadians(angle), x = radius * Math.sin(radians), z = radius * Math.cos(radians);
                    final double correctX = - x * correctCos - z * correctSin, correctZ = z * correctCos - x * correctSin;
                    location.add(correctX, 0, correctZ);
                    for (final Entity entity : entityCollection) {
                        ((Player) entity).spawnParticle(particle, location, count, correctOffsetX, offsetY, correctOffsetZ, speed, data);
                    }
                    location.subtract(correctX, 0, correctZ);
                }
            }
        }
    }

    public static <T> void play3D(@NotNull final Location location, @NotNull final Particle particle, final boolean mirror, final int count, final double offsetX, final double offsetY, final double offsetZ, final double speed, @Nullable final T data, final double startAngle, final double endAngle, double radius, final int repeat) {
        final Collection<Entity> entityCollection = TargetUtils.getPlayers(location, 20.0D, 10.0D, 20.0D, -20.0D, -10.0D, -20.0D, null);
        final double correctRadians = Math.toRadians(location.getYaw()), correctSin = Math.sin(correctRadians), correctCos = Math.cos(correctRadians);
        final double correctOffsetX = - offsetX * correctCos - offsetZ * correctSin, correctOffsetZ = offsetZ * correctCos - offsetX * correctSin;
        final double stepAngle = (endAngle - startAngle) / (double) repeat;
        for (double angle = startAngle; angle < endAngle; angle = angle + stepAngle) {
            final double radians = Math.toRadians(angle), x = radius * Math.sin(radians), z = radius * Math.cos(radians), y = Math.sin(radians);
            final double correctX = mirror ? x * correctCos + z * correctSin : - x * correctCos - z * correctSin, correctZ = mirror ? x * correctSin - z * correctCos : z * correctCos - x * correctSin;
            location.add(correctX, y, correctZ);
            for (final Entity entity : entityCollection) {
                ((Player) entity).spawnParticle(particle, location, count, correctOffsetX, offsetY, correctOffsetZ, speed, data);
            }
            location.subtract(correctX, 0, correctZ);
        }
    }

    public static <T> void play2DAnimation(@NotNull final AbstractPlugin plugin, @NotNull final Location location, @NotNull final Particle particle, @NotNull final Coordinate coordinate, final int count, final double offsetX, final double offsetY, final double offsetZ, final double speed, @Nullable final T data, final double startAngle, final double endAngle, final double radius, final int repeat, final long period) {
        final Collection<Entity> entityCollection = TargetUtils.getPlayers(location, 20.0D, 10.0D, 20.0D, -20.0D, -10.0D, -20.0D, null);
        final double correctRadians = Math.toRadians(location.getYaw()), correctSin = Math.sin(correctRadians), correctCos = Math.cos(correctRadians);
        final double correctOffsetX = - offsetX * correctCos - offsetZ * correctSin, correctOffsetZ = offsetZ * correctCos - offsetX * correctSin;
        final double stepAngle = (endAngle - startAngle) / (double) repeat;
        final AtomicDouble atomicDouble = new AtomicDouble(startAngle);
        switch (coordinate) {
            case XY -> Countdowner.start(plugin, false, repeat + 1, 0L, period, null, repeatLeft -> {
                final double radians = Math.toRadians(atomicDouble.getAndAdd(stepAngle)), x = radius * Math.sin(radians), y = radius * Math.sin(radians);
                final double correctX = - x * correctCos, correctZ = - x * correctSin;
                location.add(correctX, y, correctZ);
                for (final Entity entity : entityCollection) {
                    ((Player) entity).spawnParticle(particle, location, count, correctOffsetX, offsetY, correctOffsetZ, speed, data);
                }
                location.subtract(correctX, y, correctZ);
                return true;
            }, null);
            case YZ -> Countdowner.start(plugin, false, repeat + 1, 0L, period, null, repeatLeft -> {
                final double radians = Math.toRadians(atomicDouble.getAndAdd(stepAngle)), z = radius * Math.cos(radians), y = radius * Math.sin(radians);
                final double correctX = - z * correctSin, correctZ = z * correctCos;
                location.add(correctX, y, correctZ);
                for (final Entity entity : entityCollection) {
                    ((Player) entity).spawnParticle(particle, location, count, correctOffsetX, offsetY, correctOffsetZ, speed, data);
                }
                location.subtract(correctX, y, correctZ);
                return true;
            }, null);
            case XZ -> Countdowner.start(plugin, false, repeat + 1, 0L, period, null, repeatLeft -> {
                final double radians = Math.toRadians(atomicDouble.getAndAdd(stepAngle)), x = radius * Math.sin(radians), z = radius * Math.cos(radians);
                final double correctX = - x * correctCos - z * correctSin, correctZ = z * correctCos - x * correctSin;
                location.add(correctX, 0, correctZ);
                for (final Entity entity : entityCollection) {
                    ((Player) entity).spawnParticle(particle, location, count, correctOffsetX, offsetY, correctOffsetZ, speed, data);
                }
                location.subtract(correctX, 0, correctZ);
                return true;
            }, null);
        }
    }

    public static <T> void play3DAnimation(@NotNull final AbstractPlugin plugin, @NotNull final Location location, @NotNull final Particle particle, final boolean reverse, final int count, final double offsetX, final double offsetY, final double offsetZ, final double speed, @Nullable final T data, final double startAngle, final double endAngle, final double radius, final int repeat, final long period) {
        final Collection<Entity> entityCollection = TargetUtils.getPlayers(location, 20.0D, 10.0D, 20.0D, -20.0D, -10.0D, -20.0D, null);
        final double correctRadians = Math.toRadians(location.getYaw()), correctSin = Math.sin(correctRadians), correctCos = Math.cos(correctRadians);
        final double correctOffsetX = - offsetX * correctCos - offsetZ * correctSin, correctOffsetZ = offsetZ * correctCos - offsetX * correctSin;
        final double stepAngle = (endAngle - startAngle) / (double) repeat;
        final AtomicDouble atomicDouble = new AtomicDouble(startAngle);
        Countdowner.start(plugin, false, repeat + 1, 0L, period, null, repeatLeft -> {
            final double radians = Math.toRadians(atomicDouble.getAndAdd(stepAngle)), x = radius * Math.sin(radians), z = radius * Math.cos(radians), y = Math.sin(radians);
            final double correctX = reverse ? x * correctCos + z * correctSin : - x * correctCos - z * correctSin, correctZ = reverse ? x * correctSin - z * correctCos : z * correctCos - x * correctSin;
            location.add(correctX, y, correctZ);
            for (final Entity entity : entityCollection) {
                ((Player) entity).spawnParticle(particle, location, count, correctOffsetX, offsetY, correctOffsetZ, speed, data);
            }
            location.subtract(correctX, y, correctZ);
            return true;
        }, null);
    }
}
