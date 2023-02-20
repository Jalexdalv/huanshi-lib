package org.huanshi.mc.lib.utils;

import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public interface AimHandler<T extends LivingEntity> {
    void handle(@NotNull T t);
}
