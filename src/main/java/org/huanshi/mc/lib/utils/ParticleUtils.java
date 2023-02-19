package org.huanshi.mc.lib.utils;

import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.huanshi.mc.lib.AbstractPlugin;
import org.huanshi.mc.lib.timer.Countdowner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ParticleUtils {
    public static <T> void play(@NotNull Location location, @NotNull Particle particle, int count, double offsetX, double offsetY, double offsetZ, double speed, @Nullable T data) {
        double correctRadians = Math.toRadians(location.getYaw()), correctSin = Math.sin(correctRadians), correctCos = Math.cos(correctRadians);
        double correctOffsetX = - offsetX * correctCos - offsetZ * correctSin, correctOffsetZ = offsetZ * correctCos - offsetX * correctSin;
        TargetUtils.aimPlayers(location, 20.0D, 10.0D, 20.0D, -20.0D, -10.0D, -20.0D, null, player -> player.spawnParticle(particle, location, count, correctOffsetX, offsetY, correctOffsetZ, speed, data));
    }

    public static <T> void play2D(@NotNull Location location, @NotNull Particle particle, @NotNull Coordinate coordinate, int count, double offsetX, double offsetY, double offsetZ, double speed, @Nullable T data, double startAngle, double endAngle, double radius, int repeat) {
        Collection<Entity> entityCollection = TargetUtils.getPlayers(location, 20.0D, 10.0D, 20.0D, -20.0D, -10.0D, -20.0D, null);
        double correctRadians = Math.toRadians(location.getYaw()), correctSin = Math.sin(correctRadians), correctCos = Math.cos(correctRadians);
        double correctOffsetX = - offsetX * correctCos - offsetZ * correctSin, correctOffsetZ = offsetZ * correctCos - offsetX * correctSin;
        double stepAngle = (endAngle - startAngle) / (double) repeat;
        switch (coordinate) {
            case XY -> {
                for (double angle = startAngle; angle < endAngle; angle = angle + stepAngle) {
                    double radians = Math.toRadians(angle), x = radius * Math.sin(radians), y = radius * Math.sin(radians);
                    double correctX = - x * correctCos, correctZ = - x * correctSin;
                    location.add(correctX, y, correctZ);
                    for (Entity entity : entityCollection) {
                        ((Player) entity).spawnParticle(particle, location, count, correctOffsetX, offsetY, correctOffsetZ, speed, data);
                    }
                    location.subtract(correctX, y, correctZ);
                }
            } case YZ -> {
                for (double angle = startAngle; angle < endAngle; angle = angle + stepAngle) {
                    double radians = Math.toRadians(angle), z = radius * Math.cos(radians), y = radius * Math.sin(radians);
                    double correctX = - z * correctSin, correctZ = z * correctCos;
                    location.add(correctX, y, correctZ);
                    for (Entity entity : entityCollection) {
                        ((Player) entity).spawnParticle(particle, location, count, correctOffsetX, offsetY, correctOffsetZ, speed, data);
                    }
                    location.subtract(correctX, y, correctZ);
                }
            } case XZ -> {
                for (double angle = startAngle; angle < endAngle; angle = angle + stepAngle) {
                    double radians = Math.toRadians(angle), x = radius * Math.sin(radians), z = radius * Math.cos(radians);
                    double correctX = - x * correctCos - z * correctSin, correctZ = z * correctCos - x * correctSin;
                    location.add(correctX, 0, correctZ);
                    for (Entity entity : entityCollection) {
                        ((Player) entity).spawnParticle(particle, location, count, correctOffsetX, offsetY, correctOffsetZ, speed, data);
                    }
                    location.subtract(correctX, 0, correctZ);
                }
            }
        }
    }

    public static <T> void play3D(@NotNull Location location, @NotNull Particle particle, boolean mirror, int count, double offsetX, double offsetY, double offsetZ, double speed, @Nullable T data, double startAngle, double endAngle, double radius, int repeat) {
        Collection<Entity> entityCollection = TargetUtils.getPlayers(location, 20.0D, 10.0D, 20.0D, -20.0D, -10.0D, -20.0D, null);
        double correctRadians = Math.toRadians(location.getYaw()), correctSin = Math.sin(correctRadians), correctCos = Math.cos(correctRadians);
        double correctOffsetX = - offsetX * correctCos - offsetZ * correctSin, correctOffsetZ = offsetZ * correctCos - offsetX * correctSin;
        double stepAngle = (endAngle - startAngle) / (double) repeat;
        for (double angle = startAngle; angle < endAngle; angle = angle + stepAngle) {
            double radians = Math.toRadians(angle), x = radius * Math.sin(radians), z = radius * Math.cos(radians), y = Math.sin(radians);
            double correctX = mirror ? x * correctCos + z * correctSin : - x * correctCos - z * correctSin, correctZ = mirror ? x * correctSin - z * correctCos : z * correctCos - x * correctSin;
            location.add(correctX, y, correctZ);
            for (Entity entity : entityCollection) {
                ((Player) entity).spawnParticle(particle, location, count, correctOffsetX, offsetY, correctOffsetZ, speed, data);
            }
            location.subtract(correctX, 0, correctZ);
        }
    }

    public static <T> void play2DAnimation(@NotNull AbstractPlugin plugin, @NotNull Location location, @NotNull Particle particle, @NotNull Coordinate coordinate, int count, double offsetX, double offsetY, double offsetZ, double speed, @Nullable T data, double startAngle, double endAngle, double radius, int repeat, int period) {
        Collection<Entity> entityCollection = TargetUtils.getPlayers(location, 20.0D, 10.0D, 20.0D, -20.0D, -10.0D, -20.0D, null);
        double correctRadians = Math.toRadians(location.getYaw()), correctSin = Math.sin(correctRadians), correctCos = Math.cos(correctRadians);
        double correctOffsetX = - offsetX * correctCos - offsetZ * correctSin, correctOffsetZ = offsetZ * correctCos - offsetX * correctSin;
        double stepAngle = (endAngle - startAngle) / (double) repeat;
        AtomicDouble atomicDouble = new AtomicDouble(startAngle);
        switch (coordinate) {
            case XY -> Countdowner.start(plugin, false, repeat + 1, 0, period, null, repeatLeft -> {
                double radians = Math.toRadians(atomicDouble.getAndAdd(stepAngle)), x = radius * Math.sin(radians), y = radius * Math.sin(radians);
                double correctX = - x * correctCos, correctZ = - x * correctSin;
                location.add(correctX, y, correctZ);
                for (Entity entity : entityCollection) {
                    ((Player) entity).spawnParticle(particle, location, count, correctOffsetX, offsetY, correctOffsetZ, speed, data);
                }
                location.subtract(correctX, y, correctZ);
                return true;
            }, null);
            case YZ -> Countdowner.start(plugin, false, repeat + 1, 0, period, null, repeatLeft -> {
                double radians = Math.toRadians(atomicDouble.getAndAdd(stepAngle)), z = radius * Math.cos(radians), y = radius * Math.sin(radians);
                double correctX = - z * correctSin, correctZ = z * correctCos;
                location.add(correctX, y, correctZ);
                for (Entity entity : entityCollection) {
                    ((Player) entity).spawnParticle(particle, location, count, correctOffsetX, offsetY, correctOffsetZ, speed, data);
                }
                location.subtract(correctX, y, correctZ);
                return true;
            }, null);
            case XZ -> Countdowner.start(plugin, false, repeat + 1, 0, period, null, repeatLeft -> {
                double radians = Math.toRadians(atomicDouble.getAndAdd(stepAngle)), x = radius * Math.sin(radians), z = radius * Math.cos(radians);
                double correctX = - x * correctCos - z * correctSin, correctZ = z * correctCos - x * correctSin;
                location.add(correctX, 0, correctZ);
                for (Entity entity : entityCollection) {
                    ((Player) entity).spawnParticle(particle, location, count, correctOffsetX, offsetY, correctOffsetZ, speed, data);
                }
                location.subtract(correctX, 0, correctZ);
                return true;
            }, null);
        }
    }

    public static <T> void play3DAnimation(@NotNull AbstractPlugin plugin, @NotNull Location location, @NotNull Particle particle, boolean reverse, int count, double offsetX, double offsetY, double offsetZ, double speed, @Nullable T data, double startAngle, double endAngle, double radius, int repeat, int period) {
        Collection<Entity> entityCollection = TargetUtils.getPlayers(location, 20.0D, 10.0D, 20.0D, -20.0D, -10.0D, -20.0D, null);
        double correctRadians = Math.toRadians(location.getYaw()), correctSin = Math.sin(correctRadians), correctCos = Math.cos(correctRadians);
        double correctOffsetX = - offsetX * correctCos - offsetZ * correctSin, correctOffsetZ = offsetZ * correctCos - offsetX * correctSin;
        double stepAngle = (endAngle - startAngle) / (double) repeat;
        AtomicDouble atomicDouble = new AtomicDouble(startAngle);
        Countdowner.start(plugin, false, repeat + 1, 0, period, null, repeatLeft -> {
            double radians = Math.toRadians(atomicDouble.getAndAdd(stepAngle)), x = radius * Math.sin(radians), z = radius * Math.cos(radians), y = Math.sin(radians);
            double correctX = reverse ? x * correctCos + z * correctSin : - x * correctCos - z * correctSin, correctZ = reverse ? x * correctSin - z * correctCos : z * correctCos - x * correctSin;
            location.add(correctX, y, correctZ);
            for (Entity entity : entityCollection) {
                ((Player) entity).spawnParticle(particle, location, count, correctOffsetX, offsetY, correctOffsetZ, speed, data);
            }
            location.subtract(correctX, y, correctZ);
            return true;
        }, null);
    }
}
