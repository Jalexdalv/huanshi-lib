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
import org.bukkit.util.Vector;
import org.huanshi.mc.lib.AbstractPlugin;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.event.SkillCastEvent;
import org.huanshi.mc.lib.lang.Zh;
import org.huanshi.mc.lib.timer.CdTimer;
import org.huanshi.mc.lib.timer.TimerRunHandler;
import org.huanshi.mc.lib.timer.TimerStartHandler;
import org.huanshi.mc.lib.utils.StatusUtils;
import org.huanshi.mc.lib.utils.TargetUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
//        if ((!stun && StatusUtils.isStunning(uuid)) || (!root && StatusUtils.isRooting(uuid)) || (!silence && StatusUtils.isSilencing(uuid))) {
//            player.sendMessage(Zh.CANNOT_CAST);
//        } else
        if (CAST_MAP.containsKey(uuid)) {
            player.sendMessage(Zh.CASTING);
        } else {
            cdTimer.run(player, cd, false, null,
                () -> {
                    if (timerStartHandler == null || timerStartHandler.handle()) {
                        CAST_MAP.put(uuid, this);
                        Bukkit.getPluginManager().callEvent(new SkillCastEvent(player, this));
                        player.clearTitle();
                        player.showTitle(title);
                        try {
                            return skillRunHandler.handle();
                        } finally {
                            finish(uuid);
                        }
                    }
                    player.sendMessage(Zh.CANNOT_CAST);
                    return false;
                },
                restTime -> timerRunHandler == null || timerRunHandler.handle(restTime)
            );
        }
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

    protected void playParticle(@NotNull Location location, @NotNull Particle particle, int count) {
        location.getWorld().spawnParticle(particle, location, count);
    }

    protected void playSound(@NotNull Location location, @NotNull Sound sound, float volume, float pitch, double x1, double y1, double z1, double x2, double y2, double z2) {
        for (Entity entity : TargetUtils.getAABBEntities(location, x1, y1, z1, x2, y2, z2)) {
            if (entity instanceof Player player) {
                player.playSound(location, sound, volume, pitch);
            }
        }
    }

    protected void targetAABB(@NotNull Location location, double x1, double y1, double z1, double x2, double y2, double z2, @NotNull TargetHandler targetHandler) {
        for (Entity targetEntity : TargetUtils.getAABBEntities(location, x1, y1, z1, x2, y2, z2)) {
            if (targetEntity instanceof LivingEntity livingEntity) {
                targetHandler.handle(livingEntity);
            }
        }
    }

    protected void charge(@NotNull Player player, int repeat, long period, int accelerateRepeat, double velocity, @Nullable SkillStartHandler skillStartHandler, @Nullable SkillRunHandler skillRunHandler, @Nullable SkillFinishHandler skillFinishHandler, @Nullable SkillStartHandler accelerateStartHandler, @Nullable SkillRunHandler accelerateRunHandler, @Nullable SkillFinishHandler accelerateFinishHandler) {
        AtomicInteger atomicInteger = new AtomicInteger(repeat);
        if (skillStartHandler == null || skillStartHandler.handle()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (atomicInteger.getAndDecrement() > 0) {
                        if (skillRunHandler == null || skillRunHandler.handle()) {
                            charge(player, accelerateRepeat, velocity, accelerateStartHandler, accelerateRunHandler, accelerateFinishHandler);
                            return;
                        }
                    }
                    if (skillFinishHandler == null || skillFinishHandler.handle()) {
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0L, period);
        }
    }

    protected void charge(@NotNull Player player, int repeat, double velocity, @Nullable SkillStartHandler skillStartHandler, @Nullable SkillRunHandler skillRunHandler, @Nullable SkillFinishHandler skillFinishHandler) {
        Vector vector = player.getLocation().getDirection().multiply(velocity).setY(0.0D);
        AtomicInteger atomicInteger = new AtomicInteger(repeat);
        if (skillStartHandler == null || skillStartHandler.handle()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (atomicInteger.getAndDecrement() > 0) {
                        if (skillRunHandler == null || skillRunHandler.handle()) {
                            player.setVelocity(vector);
                            return;
                        }
                    }
                    if (skillFinishHandler == null || skillFinishHandler.handle()) {
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
                    if (atomicInteger.getAndDecrement() > 0) {
                        if (skillRunHandler == null || skillRunHandler.handle()) {
                            player.setRotation(player.getLocation().getYaw() + yaw, 0.0F);
                            return;
                        }
                    }
                    if (skillFinishHandler == null || skillFinishHandler.handle()) {
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0L, 1L);
        }
    }
}
