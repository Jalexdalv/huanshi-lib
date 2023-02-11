package org.huanshi.mc.lib.service;

import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.huanshi.mc.lib.annotation.Service;
import org.jetbrains.annotations.NotNull;

@Service
public class StrikeFlyService extends AbstractService {
    private final Vector vector = new Vector(0, 1, 0);

    public void run(@NotNull LivingEntity livingEntity, double velocity) {
        livingEntity.setVelocity(vector.multiply(velocity));
    }
}
