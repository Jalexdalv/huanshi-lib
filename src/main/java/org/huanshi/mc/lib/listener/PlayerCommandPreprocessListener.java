package org.huanshi.mc.lib.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.huanshi.mc.lib.service.CommandService;
import org.jetbrains.annotations.NotNull;

public class PlayerCommandPreprocessListener extends AbstractListener {
    @Autowired
    private CommandService commandService;

    @EventHandler
    public void onPlayerCommandPreprocess(@NotNull PlayerCommandPreprocessEvent playerCommandPreprocessEvent) {
        if (commandService.check(playerCommandPreprocessEvent.getPlayer(), playerCommandPreprocessEvent.getMessage())) {
            playerCommandPreprocessEvent.setCancelled(true);
        }
    }
}
