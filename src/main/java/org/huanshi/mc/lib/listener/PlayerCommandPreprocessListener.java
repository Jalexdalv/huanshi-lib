package org.huanshi.mc.lib.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.huanshi.mc.lib.service.CommandCheckService;
import org.jetbrains.annotations.NotNull;

public class PlayerCommandPreprocessListener extends AbstractListener {
    @Autowired
    private CommandCheckService commandCheckService;

    @EventHandler
    public void onPlayerCommandPreprocess(@NotNull PlayerCommandPreprocessEvent playerCommandPreprocessEvent) {
        if (commandCheckService.check(playerCommandPreprocessEvent.getPlayer(), playerCommandPreprocessEvent.getMessage())) {
            playerCommandPreprocessEvent.setCancelled(true);
        }
    }
}
