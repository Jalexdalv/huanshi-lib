package org.huanshi.mc.lib.service;

import lombok.Getter;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.service.AbstractService;
import org.huanshi.mc.framework.timer.TimerHelper;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractBossBarService extends AbstractService {
    @Getter
    private TimerHelper timerHelper;
    @Getter
    private final Map<UUID, BossBar> bossBarMap = new HashMap<>();

    @Override
    public void onLoad(@NotNull AbstractPlugin plugin) {
        timerHelper = new TimerHelper(plugin);
    }

    public void stop(@NotNull UUID uuid) {
        timerHelper.stop(uuid);
    }

    public void stop(@NotNull Player player) {
        stop(player.getUniqueId());
    }

    public boolean isRunning(@NotNull UUID uuid) {
        return timerHelper.isRunning(uuid);
    }

    public boolean isRunning(@NotNull Player player) {
        return isRunning(player.getUniqueId());
    }
}
