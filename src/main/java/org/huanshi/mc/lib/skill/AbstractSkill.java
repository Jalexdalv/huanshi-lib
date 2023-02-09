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

    protected void run(@NotNull Player player, int cd, boolean isStunning, boolean isRooting, boolean isSilencing, @NotNull Title title, @Nullable TimerStartHandler timerStartHandler, @Nullable TimerRunHandler timerRunHandler) {
        UUID uuid = player.getUniqueId();
        if ((!isStunning && StatusUtils.isStunning(uuid)) || (!isRooting && StatusUtils.isRooting(uuid)) || (!isSilencing && StatusUtils.isSilencing(uuid))) {
            player.sendMessage(Zh.CANNOT_CAST);
        } else if (CAST_MAP.containsKey(uuid)) {
            player.sendMessage(Zh.CASTING);
        } else {
            cdTimer.run(player, cd, false, false, null,
                () -> {
                    try {
                        CAST_MAP.put(uuid, this);
                        Bukkit.getPluginManager().callEvent(new SkillCastEvent(player, this));
                        player.clearTitle();
                        player.showTitle(title);
                        return timerStartHandler == null || timerStartHandler.handle();
                    } finally {
                        finish(uuid);
                    }
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

    protected void charge(@NotNull Player player, int repeat, long period, int accelerateRepeat, double velocity, @Nullable ChargeStartHandler chargeStartHandler, @Nullable ChargeRunHandler chargeRunHandler, @Nullable ChargeFinishHandler chargeFinishHandler, @Nullable ChargeStartHandler accelerateStartHandler, @Nullable ChargeRunHandler accelerateRunHandler, @Nullable ChargeFinishHandler accelerateFinishHandler) {
        AtomicInteger atomicInteger = new AtomicInteger(repeat);
        if (chargeStartHandler == null || chargeStartHandler.handle()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (atomicInteger.getAndDecrement() > 0) {
                        if (chargeRunHandler == null || chargeRunHandler.handle()) {
                            charge(player, accelerateRepeat, velocity, accelerateStartHandler, accelerateRunHandler, accelerateFinishHandler);
                            return;
                        }
                    }
                    if (chargeFinishHandler == null || chargeFinishHandler.handle()) {
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0L, period);
        }
    }

    protected void charge(@NotNull Player player, int repeat, double velocity, @Nullable ChargeStartHandler chargeStartHandler, @Nullable ChargeRunHandler chargeRunHandler, @Nullable ChargeFinishHandler chargeFinishHandler) {
        Vector vector = player.getLocation().getDirection().normalize().multiply(velocity).setY(0.0D);
        AtomicInteger atomicInteger = new AtomicInteger(repeat);
        if (chargeStartHandler == null || chargeStartHandler.handle()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (atomicInteger.getAndDecrement() > 0) {
                        if (chargeRunHandler == null || chargeRunHandler.handle()) {
                            player.setVelocity(vector);
                            return;
                        }
                    }
                    if (chargeFinishHandler == null || chargeFinishHandler.handle()) {
                        StatusUtils.stiff(player);
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0L, 1L);
        }
    }
}
