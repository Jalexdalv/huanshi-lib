package org.huanshi.mc.lib.service;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.service.AbstractService;
import org.huanshi.mc.framework.timer.TimerHelper;
import org.huanshi.mc.lib.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerForbidLookService extends AbstractService {
    @Autowired
    private Plugin plugin;
    private TimerHelper timerHelper;

    @Override
    public void onLoad(@NotNull AbstractPlugin plugin) {
        timerHelper = new TimerHelper(plugin);
    }

    public void start(@NotNull Player player, long duration) {
        Location location = player.getLocation();
        timerHelper.start(player.getUniqueId(), true, true, duration, 0L, 250L, null, null, durationLeft -> {
            Location newLocation = player.getLocation();
            if (location.getYaw() != newLocation.getYaw() || location.getPitch() != newLocation.getPitch()) {
                player.teleportAsync(location);
            }
            return true;
        }, null);
    }

    public void stop(@NotNull UUID uuid) {
        timerHelper.stop(uuid);
    }

    public void stop(@NotNull Player player) {
        stop(player.getUniqueId());
    }
}
