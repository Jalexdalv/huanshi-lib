package org.huanshi.mc.lib.skill;

import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public interface AimHandler {
    void handle(@NotNull LivingEntity livingEntity);
}
