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
import org.huanshi.mc.lib.timer.TimerStartHandler;
import org.huanshi.mc.lib.utils.StatusUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 抽象技能
 * @author Jalexdalv
 */
public abstract class AbstractSkill {
    private static final Map<UUID, AbstractSkill> CAST_MAP = new ConcurrentHashMap<>();
    @Autowired
    private AbstractPlugin plugin;
    private final CdTimer cdTimer = new CdTimer();

    /**
     * 加载
     */
    public void load() {}

    /**
     * 释放
     * @param player 玩家
     */
    public abstract void cast(@NotNull Player player);

    /**
     * 启动
     * @param player 玩家
     * @param cd CD（秒）
     * @param title 标题
     * @param timerStartHandler 计时器启动时处理
     * @param skillRunHandler 技能运行时处理
     */
    protected void run(@NotNull Player player, double cd, @NotNull Title title, @Nullable TimerStartHandler timerStartHandler, @NotNull SkillRunHandler skillRunHandler) {
        UUID uuid = player.getUniqueId();
        if (CAST_MAP.containsKey(uuid)) {
            player.sendMessage(Zh.CASTING);
        } else {
            cdTimer.run(player, cd, false, null,
                () -> {
                    if (timerStartHandler == null || timerStartHandler.handle()) {
                        try {
                            CAST_MAP.put(uuid, this);
                            Bukkit.getPluginManager().callEvent(new SkillCastEvent(player, this));
                            player.clearTitle();
                            player.showTitle(title);
                            return skillRunHandler.handle();
                        } finally {
                            finish(uuid);
                        }
                    }
                    player.sendMessage(Zh.CANNOT_CAST);
                    return false;
                }, null
            );
        }
    }

    /**
     * 结束
     * @param uuid UUID
     */
    protected void finish(@NotNull UUID uuid) {
        CAST_MAP.remove(uuid);
    }

    /**
     * 判断是否正在释放
     * @param uuid UUID
     * @param skill 技能
     * @return 是否正在释放
     */
    public boolean isCasting(@NotNull UUID uuid, @NotNull AbstractSkill skill) {
        return CAST_MAP.get(uuid) == skill;
    }

    /**
     * 判断是否正在释放
     * @param uuid UUID
     * @return 是否正在释放
     */
    public boolean isCasting(@NotNull UUID uuid) {
        return CAST_MAP.containsKey(uuid);
    }

    /**
     * 修复位置
     * @param location 位置
     * @param x x轴相对坐标
     * @param y y轴相对坐标
     * @param z z轴相对坐标
     * @return 位置
     */
    protected @NotNull Location fixLocation(@NotNull Location location, double x, double y, double z) {
        double radians = Math.toRadians(location.getYaw()), sin = Math.sin(radians), cos = Math.cos(radians);
        return location.clone().add(-x * cos - z * sin, y, z * cos - x * sin);
    }

    /**
     * 修复方向
     * @param location 位置
     * @param distance 距离
     * @return 方向
     */
    protected @NotNull Vector fixDirection(@NotNull Location location, double distance) {
        location.setPitch(0.0F);
        return location.getDirection().multiply(distance);
    }

    /**
     * 获取长方体区域
     * @param location 位置
     * @param x1 第一个点的x轴相对坐标
     * @param y1 第一个点的y轴相对坐标
     * @param z1 第一个点的z轴相对坐标
     * @param x2 第二个点的x轴相对坐标
     * @param y2 第二个点的y轴相对坐标
     * @param z2 第二个点的z轴相对坐标
     * @return 长方体区域
     */
    protected @NotNull BoundingBox getBoundingBox(@NotNull Location location, double x1, double y1, double z1, double x2, double y2, double z2) {
        return BoundingBox.of(fixLocation(location, x1, y1, z1), fixLocation(location, x2, y2, z2));
    }

    /**
     * 获取长方体区域内的实体集合
     * @param location 位置
     * @param x1 第一个点的x轴相对坐标
     * @param y1 第一个点的y轴相对坐标
     * @param z1 第一个点的z轴相对坐标
     * @param x2 第二个点的x轴相对坐标
     * @param y2 第二个点的y轴相对坐标
     * @param z2 第二个点的z轴相对坐标
     * @return 长方体区域内的实体集合
     */
    protected @NotNull Collection<Entity> getAABBEntities(@NotNull Location location, double x1, double y1, double z1, double x2, double y2, double z2) {
        return location.getWorld().getNearbyEntities(getBoundingBox(location, x1, y1, z1, x2, y2, z2));
    }

    /**
     * 瞄准长方体区域内的实体
     * @param location 位置
     * @param x1 第一个点的x轴相对坐标
     * @param y1 第一个点的y轴相对坐标
     * @param z1 第一个点的z轴相对坐标
     * @param x2 第二个点的x轴相对坐标
     * @param y2 第二个点的y轴相对坐标
     * @param z2 第二个点的z轴相对坐标
     * @param aimHandler 瞄准时处理
     */
    protected void aimAABBEntities(@NotNull Location location, double x1, double y1, double z1, double x2, double y2, double z2, @NotNull AimHandler aimHandler) {
        for (Entity entity : getAABBEntities(location, x1, y1, z1, x2, y2, z2)) {
            if (entity instanceof LivingEntity livingEntity) {
                aimHandler.handle(livingEntity);
            }
        }
    }

    /**
     * 播放粒子
     * @param location 位置
     * @param particle 粒子
     * @param count 数量
     * @param offsetX x轴偏移量
     * @param offsetY y轴偏移量
     * @param offsetZ z轴偏移量
     * @param speed 速度
     */
    protected void playParticle(@NotNull Location location, @NotNull Particle particle, int count, double offsetX, double offsetY, double offsetZ, double speed) {
        location.getWorld().spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed);
    }

    /**
     * 播放扇形粒子动画
     * @param location 位置
     * @param particle 粒子
     * @param count 数量
     * @param offsetX x轴偏移量
     * @param offsetY y轴偏移量
     * @param offsetZ z轴偏移量
     * @param speed 速度
     * @param angle 角度
     * @param radius 半径
     * @param repeat 重复次数
     * @param period 触发间隔（tick）
     */
    protected void playSectorParticleAnimation(@NotNull Location location, @NotNull Particle particle, int count, double offsetX, double offsetY, double offsetZ, double speed, int angle, double radius, int repeat, int period) {
        Vector vector = fixDirection(location, radius).rotateAroundY(Math.toRadians((double) angle / (double) 2));
        double stepAngle = -Math.toRadians((double) angle / (double) repeat);
        repeat(repeat + 1, period, null, () -> {
            location.add(vector);
            location.getWorld().spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed);
            location.subtract(vector);
            vector.rotateAroundY(stepAngle);
            return true;
        }, null);
    }

    /**
     * 播放音效
     * @param location 位置
     * @param sound 音效
     * @param volume 音量
     * @param pitch 音高
     */
    protected void playSound(@NotNull Location location, @NotNull Sound sound, float volume, float pitch) {
        for (Entity entity : getAABBEntities(location, 20.0D, 5.0D, 20.0D, -20.0D, -5.0D, -20.0D)) {
            if (entity instanceof Player player) {
                player.playSound(location, sound, volume, pitch);
            }
        }
    }

    /**
     * 重复
     * @param repeat 重复次数
     * @param period 触发间隔（tick）
     * @param skillStartHandler 技能启动时处理
     * @param skillRunHandler 技能运行时处理
     * @param skillFinishHandler 技能结束时处理
     */
    protected void repeat(int repeat, int period, @Nullable SkillStartHandler skillStartHandler, @Nullable SkillRunHandler skillRunHandler, @Nullable SkillFinishHandler skillFinishHandler) {
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

    /**
     * 冲刺
     * @param player 玩家
     * @param repeat 重复次数
     * @param period 触发间隔（tick）
     * @param velocityRepeat 加速次数
     * @param velocity 速度
     * @param skillStartHandler 技能启动时处理
     * @param skillRunHandler 技能运行时处理
     * @param skillFinishHandler 技能结束时处理
     * @param velocityStartHandler 加速启动时处理
     * @param velocityRunHandler 加速运行时处理
     * @param velocityFinishHandler 加速结束时处理
     */
    protected void charge(@NotNull Player player, int repeat, int period, int velocityRepeat, double velocity, @Nullable SkillStartHandler skillStartHandler, @Nullable SkillRunHandler skillRunHandler, @Nullable SkillFinishHandler skillFinishHandler, @Nullable SkillStartHandler velocityStartHandler, @Nullable SkillRunHandler velocityRunHandler, @Nullable SkillFinishHandler velocityFinishHandler) {
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

    /**
     * 冲刺
     * @param player 玩家
     * @param repeat 重复次数
     * @param velocity 速度
     * @param skillStartHandler 技能启动时处理
     * @param skillRunHandler 技能运行时处理
     * @param skillFinishHandler 技能结束时处理
     */
    protected void charge(@NotNull Player player, int repeat, double velocity, @Nullable SkillStartHandler skillStartHandler, @Nullable SkillRunHandler skillRunHandler, @Nullable SkillFinishHandler skillFinishHandler) {
        Vector vector = fixDirection(player.getLocation(), velocity);
        AtomicInteger atomicInteger = new AtomicInteger(repeat);
        if (skillStartHandler == null || skillStartHandler.handle()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (atomicInteger.getAndDecrement() > 0 && (skillRunHandler == null || skillRunHandler.handle())) {
                        player.setVelocity(vector);
                    } else if (skillFinishHandler == null || skillFinishHandler.handle()) {
                        StatusUtils.stiff(plugin, player, 0.15D);
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0L, 1L);
        }
    }

    /**
     * 旋转
     * @param player 玩家
     * @param repeat 重复次数
     * @param yaw 偏航角
     * @param skillStartHandler 技能启动时处理
     * @param skillRunHandler 技能运行时处理
     * @param skillFinishHandler 技能结束时处理
     */
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
