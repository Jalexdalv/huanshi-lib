package org.huanshi.mc.lib.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.huanshi.mc.lib.service.PlayerBlockService;
import org.jetbrains.annotations.NotNull;

public class PlayerQuitListener extends AbstractListener {
    @Autowired
    private PlayerBlockService playerBlockService;

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent playerQuitEvent) {
        playerBlockService.clear(playerQuitEvent.getPlayer());
    }
}
