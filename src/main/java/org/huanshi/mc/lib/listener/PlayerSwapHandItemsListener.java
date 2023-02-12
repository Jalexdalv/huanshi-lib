package org.huanshi.mc.lib.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Listener;
import org.huanshi.mc.lib.service.SkillService;
import org.jetbrains.annotations.NotNull;

@Listener
public class PlayerSwapHandItemsListener extends AbstractListener {
    @Autowired
    private SkillService skillService;

    @EventHandler
    public void onPlayerSwapHandItems(@NotNull PlayerSwapHandItemsEvent playerSwapHandItemsEvent) {
        if (skillService.isRunning(playerSwapHandItemsEvent.getPlayer().getUniqueId())) {
            playerSwapHandItemsEvent.setCancelled(true);
        }
    }
}
