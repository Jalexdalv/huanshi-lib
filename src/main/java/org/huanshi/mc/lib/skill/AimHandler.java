package org.huanshi.mc.lib.skill;

import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

/**
 * 瞄准处理
 * @author Jalexdalv
 */
public interface AimHandler {
    /**
     * 处理
     * @param livingEntity 活体
     */
    void handle(@NotNull LivingEntity livingEntity);
}
