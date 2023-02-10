package org.huanshi.mc.lib.skill;

import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.huanshi.mc.lib.AbstractPlugin;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.event.SkillCastEvent;
import org.huanshi.mc.lib.lang.Zh;
import org.huanshi.mc.lib.timer.CdTimer;
import org.huanshi.mc.lib.timer.TimerRunHandler;
import org.huanshi.mc.lib.timer.TimerStartHandler;
import org.huanshi.mc.lib.utils.StatusUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractSkill {
    private static final Map<UUID, AbstractSkill> CAST_MAP = new ConcurrentHashMap<>();
    @Autowired
    private AbstractPlugin plugin;
    private final CdTimer cdTimer = new CdTimer();

    public void load() {}

    public abstract void cast(@NotNull Player player);

    protected void run(@NotNull Player player, int cd, @NotNull Title title, @Nullable TimerStartHandler timerStartHandler, @NotNull SkillRunHandler skillRunHandler, @Nullable TimerRunHandler timerRunHandler) {
        UUID uuid = player.getUniqueId();
        if (CAST_MAP.containsKey(uuid)) {
            player.sendMessage(Zh.CASTING);
            return;
        }
        cdTimer.run(player, cd, false, null,
            () -> {
                if (timerStartHandler != null && !timerStartHandler.handle()) {
                    player.sendMessage(Zh.CANNOT_CAST);
                    return false;
                }
                CAST_MAP.put(uuid, this);
                Bukkit.getPluginManager().callEvent(new SkillCastEvent(player, this));
                player.clearTitle();
                player.showTitle(title);
                try {
                    return skillRunHandler.handle();
                } finally {
                    finish(uuid);
                }
            },
            restTime -> timerRunHandler == null || timerRunHandler.handle(restTime)
        );
    }

    protected void finish(@NotNull UUID uuid) {
        CAST_MAP.remove(uuid);
    }

    public boolean isCasting(@NotNull UUID uuid, @NotNull AbstractSkill skill) {
        return CAST_MAP.get(uuid) == skill;
    }

    public boolean isCasting(@NotNull UUID uuid) {
        return CAST_MAP.containsKey(uuid);
    }

    protected @NotNull Location correctLocation(@NotNull Location location, double x, double y, double z) {
        double radians = Math.toRadians(location.getYaw());
        double sin = Math.sin(radians), cos = Math.cos(radians);
        return location.clone().add(-x * cos - z * sin, y, z * cos - x * sin);
    }

    protected @NotNull Vector correctDirection(@NotNull Location location, double distance) {
        location.setPitch(0.0F);
        return location.getDirection().multiply(distance);
    }

    protected @NotNull BoundingBox getBoundingBox(@NotNull Location location, double x1, double y1, double z1, double x2, double y2, double z2) {
        return BoundingBox.of(correctLocation(location, x1, y1, z1), correctLocation(location, x2, y2, z2));
    }

    protected @NotNull Collection<Entity> getAABBEntities(@NotNull Location location, double x1, double y1, double z1, double x2, double y2, double z2) {
        return location.getWorld().getNearbyEntities(getBoundingBox(location, x1, y1, z1, x2, y2, z2));
    }

    protected void aimAABBEntities(@NotNull Location location, double x1, double y1, double z1, double x2, double y2, double z2, @NotNull AimHandler aimHandler) {
        for (Entity targetEntity : getAABBEntities(location, x1, y1, z1, x2, y2, z2)) {
            if (targetEntity instanceof LivingEntity livingEntity) {
                aimHandler.handle(livingEntity);
            }
        }
    }

    protected void playParticle(@NotNull Location location, @NotNull Particle particle, int count, double offsetX, double offsetY, double offsetZ, double speed) {
        location.getWorld().spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed);
    }

    protected void playSectorParticle(@NotNull Location location, @NotNull Particle particle, int angle, double radius, int times, int count, double offsetX, double offsetY, double offsetZ, double speed) {
        Vector vector = correctDirection(location, radius).rotateAroundY(Math.toRadians((double) angle / (double) 2));
        int step = angle / times;
        double stepAngle = Math.toRadians(-step);
        for (int i = 0; i < angle; i = i + step) {
            vector.rotateAroundY(stepAngle);
            location.add(vector);
            location.getWorld().spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed);
            location.subtract(vector);
        }
    }

    protected void playSound(@NotNull Location location, @NotNull Sound sound, float volume, float pitch) {
        for (Entity entity : getAABBEntities(location, 20.0D, 5.0D, 20.0D, -20.0D, -5.0D, -20.0D)) {
            if (entity instanceof Player player) {
                player.playSound(location, sound, volume, pitch);
            }
        }
    }

    protected void repeat(int repeat, long period, @Nullable SkillStartHandler skillStartHandler, @Nullable SkillRunHandler skillRunHandler, @Nullable SkillFinishHandler skillFinishHandler) {
        AtomicInteger atomicInteger = new AtomicInteger(repeat);
        if (skillStartHandler == null || skillStartHandler.handle()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if ((atomicInteger.getAndDecrement() <= 0 || (skillRunHandler != null && !skillRunHandler.handle())) && (skillFinishHandler == null || skillFinishHandler.handle())) {
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0L, period);
        }
    }

    protected void charge(@NotNull Player player, int repeat, long period, int velocityRepeat, double velocity, @Nullable SkillStartHandler skillStartHandler, @Nullable SkillRunHandler skillRunHandler, @Nullable SkillFinishHandler skillFinishHandler, @Nullable SkillStartHandler velocityStartHandler, @Nullable SkillRunHandler velocityRunHandler, @Nullable SkillFinishHandler velocityFinishHandler) {
        AtomicInteger atomicInteger = new AtomicInteger(repeat);
        if (skillStartHandler == null || skillStartHandler.handle()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (atomicInteger.getAndDecrement() > 0 && (skillRunHandler == null || skillRunHandler.handle())) {
                        charge(player, velocityRepeat, velocity, velocityStartHandler, velocityRunHandler, velocityFinishHandler);
                    } else if (skillFinishHandler == null || skillFinishHandler.handle()) {
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0L, period);
        }
    }

    protected void charge(@NotNull Player player, int repeat, double velocity, @Nullable SkillStartHandler skillStartHandler, @Nullable SkillRunHandler skillRunHandler, @Nullable SkillFinishHandler skillFinishHandler) {
        Vector vector = correctDirection(player.getLocation(), velocity);
        AtomicInteger atomicInteger = new AtomicInteger(repeat);
        if (skillStartHandler == null || skillStartHandler.handle()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (atomicInteger.getAndDecrement() > 0 && (skillRunHandler == null || skillRunHandler.handle())) {
                        player.setVelocity(vector);
                    } else if (skillFinishHandler == null || skillFinishHandler.handle()) {
                        StatusUtils.stiff(player);
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0L, 1L);
        }
    }

    protected void rotation(@NotNull Player player, int repeat, float yaw, @Nullable SkillStartHandler skillStartHandler, @Nullable SkillRunHandler skillRunHandler, @Nullable SkillFinishHandler skillFinishHandler) {
        AtomicInteger atomicInteger = new AtomicInteger(repeat);
        if (skillStartHandler == null || skillStartHandler.handle()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (atomicInteger.getAndDecrement() > 0 && (skillRunHandler == null || skillRunHandler.handle())) {
                        player.setRotation(player.getLocation().getYaw() + yaw, 0.0F);
                    } else if (skillFinishHandler == null || skillFinishHandler.handle()) {
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0L, 1L);
        }
    }
}
