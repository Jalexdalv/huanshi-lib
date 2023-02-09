package org.huanshi.mc.lib.skill;

import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public interface TargetHandler {
    void handle(@NotNull LivingEntity livingEntity);
}
