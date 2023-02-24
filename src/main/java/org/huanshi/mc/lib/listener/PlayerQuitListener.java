package org.huanshi.mc.lib.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.huanshi.mc.lib.service.BlockService;
import org.jetbrains.annotations.NotNull;

public class PlayerQuitListener extends AbstractListener {
    @Autowired
    private BlockService blockService;

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent playerQuitEvent) {
        blockService.clear(playerQuitEvent.getPlayer());
    }
}
