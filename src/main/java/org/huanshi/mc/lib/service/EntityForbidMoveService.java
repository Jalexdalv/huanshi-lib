package org.huanshi.mc.lib.service;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.service.AbstractService;
import org.huanshi.mc.framework.timer.TimerHelper;
import org.huanshi.mc.lib.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class EntityForbidMoveService extends AbstractService {
    @Autowired
    private Plugin plugin;
    private TimerHelper timerHelper;

    @Override
    public void onLoad(@NotNull AbstractPlugin plugin) {
        timerHelper = new TimerHelper(plugin);
    }

    public void start(@NotNull Entity entity, long duration) {
        Location location = entity.getLocation();
        timerHelper.start(entity.getUniqueId(), true, true, duration, 0L, 250L, null, null, durationLeft -> {
            Location newLocation = entity.getLocation();
            if (location.x() != newLocation.x() || location.y() != newLocation.y() || location.z() != newLocation.z()) {
                entity.teleportAsync(location);
            }
            return true;
        }, null);
    }

    public void stop(@NotNull UUID uuid) {
        timerHelper.stop(uuid);
    }

    public void stop(@NotNull Entity entity) {
        stop(entity.getUniqueId());
    }
}
