package org.huanshi.mc.lib.skill;

import com.google.common.util.concurrent.AtomicDouble;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.huanshi.mc.lib.AbstractPlugin;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.event.SkillCastEvent;
import org.huanshi.mc.lib.service.SkillService;
import org.huanshi.mc.lib.timer.CdTimer;
import org.huanshi.mc.lib.utils.RepeatFinishHandler;
import org.huanshi.mc.lib.utils.RepeatRunHandler;
import org.huanshi.mc.lib.utils.RepeatStartHandler;
import org.huanshi.mc.lib.utils.TargetUtils;
import org.huanshi.mc.lib.utils.TimerUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * 抽象技能
 * @author Jalexdalv
 */
public abstract class AbstractSkill {
    @Autowired
    private AbstractPlugin plugin;
    @Autowired
    private SkillService skillService;
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
     * 释放
     * @param player 玩家
     * @param cd CD（毫秒）
     * @param title 标题
     * @param skillCastHandler 技能释放时处理
     */
    protected void cast(@NotNull Player player, long cd, @NotNull Title title, @NotNull SkillCastHandler skillCastHandler) {
        UUID uuid = player.getUniqueId();
        if (!skillService.isCasting(uuid) && canCast(player)) {
            cdTimer.run(player, cd, false, null, () -> {
                skillService.cast(uuid, this);
                Bukkit.getPluginManager().callEvent(new SkillCastEvent(player, this));
                player.clearTitle();
                player.showTitle(title);
                return skillCastHandler.handle();
            }, null);
        }
    }

    /**
     * 判断是否可以释放
     * @param player 玩家
     * @return 是否可以释放
     */
    protected boolean canCast(@NotNull Player player) {
        return true;
    }

    /**
     * 判断是否可以继续释放
     * @param player 玩家
     * @return 是否可以继续释放
     */
    protected boolean canProceed(@NotNull Player player) {
        return true;
    }

    /**
     * 瞄准长方体区域内的活体
     * @param location 位置
     * @param x1 第一个点的x轴相对坐标
     * @param y1 第一个点的y轴相对坐标
     * @param z1 第一个点的z轴相对坐标
     * @param x2 第二个点的x轴相对坐标
     * @param y2 第二个点的y轴相对坐标
     * @param z2 第二个点的z轴相对坐标
     * @param aimHandler 瞄准时处理
     */
    protected void aimAABBLivingEntities(@NotNull Location location, double x1, double y1, double z1, double x2, double y2, double z2, @NotNull AimHandler aimHandler) {
        for (Entity entity : TargetUtils.getAABBEntities(location, x1, y1, z1, x2, y2, z2)) {
            if (entity instanceof LivingEntity livingEntity) {
                aimHandler.handle(livingEntity);
            }
        }
    }

    /**
     * 播放粒子（40*40*20长方体区域内）
     * @param location 位置
     * @param particle 粒子
     * @param count 数量
     * @param offsetX x轴偏移量
     * @param offsetY y轴偏移量
     * @param offsetZ z轴偏移量
     * @param speed 速度
     * @param data 数据
     * @param <T> 类型
     */
    protected <T> void playParticle(@NotNull Location location, @NotNull Particle particle, int count, double offsetX, double offsetY, double offsetZ, double speed, @Nullable T data) {
        for (Entity entity : TargetUtils.getAABBEntities(location, 20.0D, 10D, 20.0D, -20.0D, -10D, -20.0D)) {
            if (entity instanceof Player player) {
                player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed, data);
            }
        }
    }

    /**
     * 播放2D粒子动画（40*40*20长方体区域内）
     * @param location 位置
     * @param particle 粒子
     * @param coordinate 坐标轴
     * @param count 数量
     * @param offsetX x轴偏移量
     * @param offsetY y轴偏移量
     * @param offsetZ z轴偏移量
     * @param speed 速度
     * @param data 数据
     * @param startAngle 开始角度
     * @param endAngle 结束角度
     * @param radius 半径
     * @param repeat 重复次数
     * @param period 间隔（tick）
     * @param <T> 类型
     */
    protected <T> void play2DParticleAnimation(@NotNull Location location, @NotNull Particle particle, @NotNull Coordinate coordinate, int count, double offsetX, double offsetY, double offsetZ, double speed, @Nullable T data, double startAngle, double endAngle, double radius, int repeat, int period) {
        AtomicDouble atomicDouble = new AtomicDouble(startAngle);
        double stepAngle = (endAngle - startAngle) / (double) repeat, fixRadians = Math.toRadians(location.getYaw()), fixSin = Math.sin(fixRadians), fixCos = Math.cos(fixRadians);
        switch (coordinate) {
            case XY -> TimerUtils.repeat(plugin, false, repeat + 1, 0, period, null, restTimes -> {
                double radians = Math.toRadians(atomicDouble.getAndAdd(stepAngle)), x = radius * Math.sin(radians), y = radius * Math.sin(radians), fixX = - x * fixCos, fixZ = - x * fixSin;
                location.add(fixX, y, fixZ);
                playParticle(location, particle, count, offsetX, offsetY, offsetZ, speed, data);
                location.subtract(fixX, y, fixZ);
                return true;
            }, null);
            case YZ -> TimerUtils.repeat(plugin, false, repeat + 1, 0, period, null, restTimes -> {
                double radians = Math.toRadians(atomicDouble.getAndAdd(stepAngle)), z = radius * Math.cos(radians), y = radius * Math.sin(radians), fixX = - z * fixSin, fixZ = z * fixCos;
                location.add(fixX, y, fixZ);
                playParticle(location, particle, count, offsetX, offsetY, offsetZ, speed, data);
                location.subtract(fixX, y, fixZ);
                return true;
            }, null);
            case XZ -> TimerUtils.repeat(plugin, false, repeat + 1, 0, period, null, restTimes -> {
                double radians = Math.toRadians(atomicDouble.getAndAdd(stepAngle)), x = radius * Math.sin(radians), z = radius * Math.cos(radians), fixX = - x * fixCos - z * fixSin, fixZ = z * fixCos - x * fixSin;
                location.add(fixX, 0, fixZ);
                playParticle(location, particle, count, offsetX, offsetY, offsetZ, speed, data);
                location.subtract(fixX, 0, fixZ);
                return true;
            }, null);
        }
    }

    /**
     * 播放3D粒子动画（40*40*20长方体区域内）
     * @param location 位置
     * @param particle 粒子
     * @param reverse 是否反向
     * @param count 数量
     * @param offsetX x轴偏移量
     * @param offsetY y轴偏移量
     * @param offsetZ z轴偏移量
     * @param speed 速度
     * @param data 数据
     * @param startAngle 开始角度
     * @param endAngle 结束角度
     * @param radius 半径
     * @param repeat 重复次数
     * @param period 间隔（tick）
     * @param <T> 类型
     */
    protected <T> void play3DParticleAnimation(@NotNull Location location, @NotNull Particle particle, boolean reverse, int count, double offsetX, double offsetY, double offsetZ, double speed, @Nullable T data, double startAngle, double endAngle, double radius, int repeat, int period) {
        AtomicDouble atomicDouble = new AtomicDouble(startAngle);
        double stepAngle = (endAngle - startAngle) / (double) repeat, fixRadians = Math.toRadians(location.getYaw()), fixSin = Math.sin(fixRadians), fixCos = Math.cos(fixRadians);
        TimerUtils.repeat(plugin, false, repeat + 1, 0, period, null, restTimes -> {
            double radians = Math.toRadians(atomicDouble.getAndAdd(stepAngle)), x = radius * Math.sin(radians), z = radius * Math.cos(radians), y = Math.sin(radians), fixX = reverse ? x * fixCos + z * fixSin : - x * fixCos - z * fixSin, fixZ = reverse ? x * fixSin - z * fixCos : z * fixCos - x * fixSin;
            location.add(fixX, y, fixZ);
            playParticle(location, particle, count, offsetX, offsetY, offsetZ, speed, data);
            location.subtract(fixX, y, fixZ);
            return true;
        }, null);
    }

    /**
     * 播放音效（40*40*20长方体区域内）
     * @param location 位置
     * @param sound 音效
     * @param volume 音量
     * @param pitch 音高
     */
    protected void playSound(@NotNull Location location, @NotNull Sound sound, float volume, float pitch) {
        for (Entity entity : TargetUtils.getAABBEntities(location, 20.0D, 10D, 20.0D, -20.0D, -10D, -20.0D)) {
            if (entity instanceof Player player) {
                player.playSound(location, sound, volume, pitch);
            }
        }
    }

    /**
     * 冲刺
     * @param player 玩家
     * @param repeat 重复次数
     * @param delay 延迟（tick）
     * @param period 间隔（tick）
     * @param velocity 速度
     * @param repeatStartHandler 重复启动时处理
     * @param repeatRunHandler 重复运行时处理
     * @param repeatFinishHandler 重复结束时处理
     */
    protected void charge(@NotNull Player player, int repeat, int delay, int period, double velocity, @Nullable RepeatStartHandler repeatStartHandler, @Nullable RepeatRunHandler repeatRunHandler, @Nullable RepeatFinishHandler repeatFinishHandler) {
        Vector vector = TargetUtils.correctDirection(player.getLocation(), velocity);
        TimerUtils.repeat(plugin, false, repeat, delay, period, repeatStartHandler, restTimes -> {
            if (canProceed(player) && (repeatRunHandler == null || repeatRunHandler.handle(restTimes))) {
                player.setVelocity(vector);
                return true;
            }
            return false;
        }, repeatFinishHandler);
    }

    /**
     * 旋转
     * @param player 玩家
     * @param repeat 重复次数
     * @param delay 延迟（tick）
     * @param period 间隔（tick）
     * @param yaw 偏航角
     * @param repeatStartHandler 重复启动时处理
     * @param repeatRunHandler 重复运行时处理
     * @param repeatFinishHandler 重复结束时处理
     */
    protected void rotation(@NotNull Player player, int repeat, int delay, int period, float yaw, @Nullable RepeatStartHandler repeatStartHandler, @Nullable RepeatRunHandler repeatRunHandler, @Nullable RepeatFinishHandler repeatFinishHandler) {
        TimerUtils.repeat(plugin, false, repeat, delay, period, repeatStartHandler, restTimes -> {
            if (canProceed(player) && (repeatRunHandler == null || repeatRunHandler.handle(restTimes))) {
                player.setRotation(player.getLocation().getYaw() + yaw, 0.0F);
                return true;
            }
            return false;
        }, repeatFinishHandler);
    }
}
