package org.huanshi.mc.lib.service;

import com.google.common.util.concurrent.AtomicDouble;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.huanshi.mc.lib.Plugin;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Service;
import org.huanshi.mc.lib.event.SkillCastEvent;
import org.huanshi.mc.lib.lang.Zh;
import org.huanshi.mc.lib.timer.CdTimer;
import org.huanshi.mc.lib.timer.Timer;
import org.huanshi.mc.lib.utils.RepeatFinishHandler;
import org.huanshi.mc.lib.utils.RepeatRunHandler;
import org.huanshi.mc.lib.utils.RepeatStartHandler;
import org.huanshi.mc.lib.utils.TargetUtils;
import org.huanshi.mc.lib.utils.TimerUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SkillService extends AbstractService {
    private static final Map<UUID, SkillService> CAST_MAP = new WeakHashMap<>();
    @Autowired
    private Plugin plugin;
    @Autowired(file = "config.yml", path = "stun.period")
    private int period;
    @Autowired(file = "config.yml", path = "root.period")
    private int rootPeriod;
    @Autowired(file = "config.yml", path = "silence.period")
    private int silencePeriod;
    @Autowired(file = "config.yml", path = "steady.period")
    private int steadyPeriod;
    @Autowired(file = "config.yml", path = "invincible.period")
    private int invinciblePeriod;
    private final Map<UUID, BossBar> stunMap = new ConcurrentHashMap<>(), rootMap = new ConcurrentHashMap<>(), silenceMap = new ConcurrentHashMap<>(), steadyMap = new ConcurrentHashMap<>(), invincibleMap = new ConcurrentHashMap<>();
    private final Timer stunTimer = new Timer(), rootTimer = new Timer(), silenceTimer = new Timer(), steadyTimer = new Timer(), invincibleTimer = new Timer();
    private final CdTimer cdTimer = new CdTimer(), stiffCdTimer = new CdTimer();
    private final Vector strikeFlyVector = new Vector(0, 1, 0);

    public void cast(@NotNull Player player) {}

    protected boolean canCast(@NotNull Player player) {
        return true;
    }

    protected boolean canProceed(@NotNull Player player) {
        return true;
    }

    protected void run(@NotNull Player player, long cd, @NotNull Title title, @NotNull SkillCastHandler skillCastHandler) {
        UUID uuid = player.getUniqueId();
        if (!isRunning(uuid) && canCast(player)) {
            cdTimer.run(uuid, cd, false, null, () -> {
                CAST_MAP.put(uuid, this);
                Bukkit.getPluginManager().callEvent(new SkillCastEvent(player, this));
                player.clearTitle();
                player.showTitle(title);
                return skillCastHandler.handle();
            }, remainDuration -> player.sendActionBar(Zh.cd(remainDuration)));
        }
    }

    protected void clear(@NotNull UUID uuid) {
        CAST_MAP.remove(uuid);
    }

    public boolean isRunning(@NotNull UUID uuid) {
        return CAST_MAP.containsKey(uuid);
    }

    public boolean isRunning(@NotNull UUID uuid, @NotNull SkillService skillService) {
        return CAST_MAP.get(uuid) == skillService;
    }

    protected void stun(@NotNull Player player, long duration, boolean force) {
        UUID uuid = player.getUniqueId();
        if (force || (!steadyTimer.isRunning(uuid) && !invincibleTimer.isRunning(uuid))) {
            stunTimer.run(plugin, uuid, true, true, duration, 0, period, null,
                () -> {
                    stunMap.putIfAbsent(uuid, BossBar.bossBar(Component.empty(), 1.0F, BossBar.Color.RED, BossBar.Overlay.NOTCHED_6));
                    return true;
                }, remainDuration -> player.showBossBar(stunMap.get(uuid).name(Zh.stun(remainDuration)).progress((float) remainDuration / (float) duration)),
                () -> {
                    player.hideBossBar(stunMap.get(uuid));
                    return true;
                }
            );
        }
    }

    protected boolean isStunning(@NotNull UUID uuid) {
        return stunTimer.isRunning(uuid);
    }

    protected void root(@NotNull Player player, long duration, boolean force) {
        UUID uuid = player.getUniqueId();
        if (force || (!steadyTimer.isRunning(uuid) && !invincibleTimer.isRunning(uuid))) {
            rootTimer.run(plugin, uuid, true, true, duration, 0, period, null,
                () -> {
                    rootMap.putIfAbsent(uuid, BossBar.bossBar(Component.empty(), 1.0F, BossBar.Color.PURPLE, BossBar.Overlay.NOTCHED_6));
                    return true;
                }, remainDuration -> player.showBossBar(rootMap.get(uuid).name(Zh.stun(remainDuration)).progress((float) remainDuration / (float) duration)),
                () -> {
                    player.hideBossBar(rootMap.get(uuid));
                    return true;
                }
            );
        }
    }

    protected boolean isRooting(@NotNull UUID uuid) {
        return rootTimer.isRunning(uuid);
    }

    protected void silence(@NotNull Player player, long duration, boolean force) {
        UUID uuid = player.getUniqueId();
        if (force || (!steadyTimer.isRunning(uuid) && !invincibleTimer.isRunning(uuid))) {
            silenceTimer.run(plugin, uuid, true, true, duration, 0, period, null,
                () -> {
                    silenceMap.putIfAbsent(uuid, BossBar.bossBar(Component.empty(), 1.0F, BossBar.Color.PINK, BossBar.Overlay.NOTCHED_6));
                    return true;
                }, remainDuration -> player.showBossBar(silenceMap.get(uuid).name(Zh.stun(remainDuration)).progress((float) remainDuration / (float) duration)),
                () -> {
                    player.hideBossBar(silenceMap.get(uuid));
                    return true;
                }
            );
        }
    }

    protected boolean isSilencing(@NotNull UUID uuid) {
        return silenceTimer.isRunning(uuid);
    }

    protected void steady(@NotNull Player player, long duration) {
        UUID uuid = player.getUniqueId();
        purify(uuid);
        steadyTimer.run(plugin, uuid, true, true, duration, 0, period, null,
            () -> {
                steadyMap.putIfAbsent(uuid, BossBar.bossBar(Component.empty(), 1.0F, BossBar.Color.GREEN, BossBar.Overlay.NOTCHED_6));
                return true;
            }, remainDuration -> player.showBossBar(steadyMap.get(uuid).name(Zh.stun(remainDuration)).progress((float) remainDuration / (float) duration)),
            () -> {
                player.hideBossBar(steadyMap.get(uuid));
                return true;
            }
        );
    }

    protected boolean isSteadying(@NotNull UUID uuid) {
        return steadyTimer.isRunning(uuid);
    }

    protected void invincible(@NotNull Player player, long duration) {
        UUID uuid = player.getUniqueId();
        purify(uuid);
        invincibleTimer.run(plugin, uuid, true, true, duration, 0, period, null,
            () -> {
                invincibleMap.putIfAbsent(uuid, BossBar.bossBar(Component.empty(), 1.0F, BossBar.Color.WHITE, BossBar.Overlay.NOTCHED_6));
                return true;
            }, remainDuration -> player.showBossBar(invincibleMap.get(uuid).name(Zh.stun(remainDuration)).progress((float) remainDuration / (float) duration)),
            () -> {
                player.hideBossBar(invincibleMap.get(uuid));
                return true;
            }
        );
    }

    protected boolean isInvincibling(@NotNull UUID uuid) {
        return invincibleTimer.isRunning(uuid);
    }

    protected void stiff(@NotNull Player player, long duration) {
        stiffCdTimer.run(player.getUniqueId(), duration, true, null, null, null);
    }

    protected void strikeFly(@NotNull LivingEntity livingEntity, double velocity) {
        livingEntity.setVelocity(strikeFlyVector.clone().multiply(velocity));
    }

    protected void purify(@NotNull UUID uuid) {
        stunTimer.clear(uuid);
        rootTimer.clear(uuid);
        silenceTimer.clear(uuid);
    }

    protected <T> void playParticle(@NotNull Location location, @NotNull Particle particle, int count, double offsetX, double offsetY, double offsetZ, double speed, @Nullable T data) {
        for (Entity entity : TargetUtils.getAABBEntities(location, 20.0D, 10D, 20.0D, -20.0D, -10D, -20.0D)) {
            if (entity instanceof Player player) {
                player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed, data);
            }
        }
    }

    protected <T> void play2DParticle(@NotNull Location location, @NotNull Particle particle, @NotNull Coordinate coordinate, int count, double offsetX, double offsetY, double offsetZ, double speed, @Nullable T data, double startAngle, double endAngle, double radius, int repeat) {
        double stepAngle = (endAngle - startAngle) / (double) repeat, fixRadians = Math.toRadians(location.getYaw()), fixSin = Math.sin(fixRadians), fixCos = Math.cos(fixRadians);
        switch (coordinate) {
            case XY -> {
                for (double angle = startAngle; angle < endAngle; angle = angle + stepAngle) {
                    double radians = Math.toRadians(angle), x = radius * Math.sin(radians), y = radius * Math.sin(radians), fixX = - x * fixCos, fixZ = - x * fixSin;
                    location.add(fixX, y, fixZ);
                    playParticle(location, particle, count, offsetX, offsetY, offsetZ, speed, data);
                    location.subtract(fixX, y, fixZ);
                }
            }
            case YZ -> {
                for (double angle = startAngle; angle < endAngle; angle = angle + stepAngle) {
                    double radians = Math.toRadians(angle), z = radius * Math.cos(radians), y = radius * Math.sin(radians), fixX = - z * fixSin, fixZ = z * fixCos;
                    location.add(fixX, y, fixZ);
                    playParticle(location, particle, count, offsetX, offsetY, offsetZ, speed, data);
                    location.subtract(fixX, y, fixZ);
                }
            }
            case XZ -> {
                for (double angle = startAngle; angle < endAngle; angle = angle + stepAngle) {
                    double radians = Math.toRadians(angle), x = radius * Math.sin(radians), z = radius * Math.cos(radians), fixX = - x * fixCos - z * fixSin, fixZ = z * fixCos - x * fixSin;
                    location.add(fixX, 0, fixZ);
                    playParticle(location, particle, count, offsetX, offsetY, offsetZ, speed, data);
                    location.subtract(fixX, 0, fixZ);
                }
            }
        }
    }

    protected <T> void play3DParticle(@NotNull Location location, @NotNull Particle particle, boolean reverse, int count, double offsetX, double offsetY, double offsetZ, double speed, @Nullable T data, double startAngle, double endAngle, double radius, int repeat) {
        double stepAngle = (endAngle - startAngle) / (double) repeat, fixRadians = Math.toRadians(location.getYaw()), fixSin = Math.sin(fixRadians), fixCos = Math.cos(fixRadians);
        for (double angle = startAngle; angle < endAngle; angle = angle + stepAngle) {
            double radians = Math.toRadians(angle), x = radius * Math.sin(radians), z = radius * Math.cos(radians), y = Math.sin(radians), fixX = reverse ? x * fixCos + z * fixSin : - x * fixCos - z * fixSin, fixZ = reverse ? x * fixSin - z * fixCos : z * fixCos - x * fixSin;
            location.add(fixX, y, fixZ);
            playParticle(location, particle, count, offsetX, offsetY, offsetZ, speed, data);
            location.subtract(fixX, 0, fixZ);
        }
    }

    protected <T> void play2DParticleAnimation(@NotNull Location location, @NotNull Particle particle, @NotNull Coordinate coordinate, int count, double offsetX, double offsetY, double offsetZ, double speed, @Nullable T data, double startAngle, double endAngle, double radius, int repeat, int period) {
        AtomicDouble atomicDouble = new AtomicDouble(startAngle);
        double stepAngle = (endAngle - startAngle) / (double) repeat, fixRadians = Math.toRadians(location.getYaw()), fixSin = Math.sin(fixRadians), fixCos = Math.cos(fixRadians);
        switch (coordinate) {
            case XY -> TimerUtils.repeat(plugin, false, repeat + 1, 0, period, null, remainRepeat -> {
                double radians = Math.toRadians(atomicDouble.getAndAdd(stepAngle)), x = radius * Math.sin(radians), y = radius * Math.sin(radians), fixX = - x * fixCos, fixZ = - x * fixSin;
                location.add(fixX, y, fixZ);
                playParticle(location, particle, count, offsetX, offsetY, offsetZ, speed, data);
                location.subtract(fixX, y, fixZ);
                return true;
            }, null);
            case YZ -> TimerUtils.repeat(plugin, false, repeat + 1, 0, period, null, remainRepeat -> {
                double radians = Math.toRadians(atomicDouble.getAndAdd(stepAngle)), z = radius * Math.cos(radians), y = radius * Math.sin(radians), fixX = - z * fixSin, fixZ = z * fixCos;
                location.add(fixX, y, fixZ);
                playParticle(location, particle, count, offsetX, offsetY, offsetZ, speed, data);
                location.subtract(fixX, y, fixZ);
                return true;
            }, null);
            case XZ -> TimerUtils.repeat(plugin, false, repeat + 1, 0, period, null, remainRepeat -> {
                double radians = Math.toRadians(atomicDouble.getAndAdd(stepAngle)), x = radius * Math.sin(radians), z = radius * Math.cos(radians), fixX = - x * fixCos - z * fixSin, fixZ = z * fixCos - x * fixSin;
                location.add(fixX, 0, fixZ);
                playParticle(location, particle, count, offsetX, offsetY, offsetZ, speed, data);
                location.subtract(fixX, 0, fixZ);
                return true;
            }, null);
        }
    }

    protected <T> void play3DParticleAnimation(@NotNull Location location, @NotNull Particle particle, boolean reverse, int count, double offsetX, double offsetY, double offsetZ, double speed, @Nullable T data, double startAngle, double endAngle, double radius, int repeat, int period) {
        AtomicDouble atomicDouble = new AtomicDouble(startAngle);
        double stepAngle = (endAngle - startAngle) / (double) repeat, fixRadians = Math.toRadians(location.getYaw()), fixSin = Math.sin(fixRadians), fixCos = Math.cos(fixRadians);
        TimerUtils.repeat(plugin, false, repeat + 1, 0, period, null, remainRepeat -> {
            double radians = Math.toRadians(atomicDouble.getAndAdd(stepAngle)), x = radius * Math.sin(radians), z = radius * Math.cos(radians), y = Math.sin(radians), fixX = reverse ? x * fixCos + z * fixSin : - x * fixCos - z * fixSin, fixZ = reverse ? x * fixSin - z * fixCos : z * fixCos - x * fixSin;
            location.add(fixX, y, fixZ);
            playParticle(location, particle, count, offsetX, offsetY, offsetZ, speed, data);
            location.subtract(fixX, y, fixZ);
            return true;
        }, null);
    }

    protected void playSound(@NotNull Location location, @NotNull Sound sound, float volume, float pitch) {
        for (Entity entity : TargetUtils.getAABBEntities(location, 20.0D, 10D, 20.0D, -20.0D, -10D, -20.0D)) {
            if (entity instanceof Player player) {
                player.playSound(location, sound, volume, pitch);
            }
        }
    }

    protected void charge(@NotNull Player player, int repeat, int delay, int period, double velocity, @Nullable RepeatStartHandler repeatStartHandler, @Nullable RepeatRunHandler repeatRunHandler, @Nullable RepeatFinishHandler repeatFinishHandler) {
        Vector vector = TargetUtils.correctDirection(player.getLocation(), velocity);
        TimerUtils.repeat(plugin, false, repeat, delay, period, repeatStartHandler, remainRepeat -> {
            if (canProceed(player) && (repeatRunHandler == null || repeatRunHandler.handle(remainRepeat))) {
                player.setVelocity(vector);
                return true;
            }
            return false;
        }, repeatFinishHandler);
    }

    protected void rotation(@NotNull Player player, int repeat, int delay, int period, float yaw, @Nullable RepeatStartHandler repeatStartHandler, @Nullable RepeatRunHandler repeatRunHandler, @Nullable RepeatFinishHandler repeatFinishHandler) {
        TimerUtils.repeat(plugin, false, repeat, delay, period, repeatStartHandler, remainRepeat -> {
            if (canProceed(player) && (repeatRunHandler == null || repeatRunHandler.handle(remainRepeat))) {
                player.setRotation(player.getLocation().getYaw() + yaw, 0.0F);
                return true;
            }
            return false;
        }, repeatFinishHandler);
    }
}
