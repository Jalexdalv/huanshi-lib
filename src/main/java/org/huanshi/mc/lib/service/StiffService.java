package org.huanshi.mc.lib.service;

import org.bukkit.entity.Player;
import org.huanshi.mc.lib.annotation.Service;
import org.huanshi.mc.lib.timer.CdTimer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Service
public class StiffService extends AbstractService {
    private final CdTimer cdTimer = new CdTimer();

    public void run(@NotNull Player player, long duration) {
        cdTimer.run(player, duration, true, null, null, null);
    }

    public void clear(@NotNull UUID uuid) {
        cdTimer.clear(uuid);
    }

    public boolean isRunning(@NotNull UUID uuid) {
        return cdTimer.isRunning(uuid);
    }
}
