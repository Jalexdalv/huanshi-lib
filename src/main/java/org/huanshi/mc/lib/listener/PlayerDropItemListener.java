package org.huanshi.mc.lib.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Listener;
import org.huanshi.mc.lib.service.SkillService;
import org.jetbrains.annotations.NotNull;

@Listener
public class PlayerDropItemListener extends AbstractListener {
    @Autowired
    private SkillService skillService;

    @EventHandler
    public void onPlayerDropItem(@NotNull PlayerDropItemEvent playerDropItemEvent) {
        if (skillService.isRunning(playerDropItemEvent.getPlayer().getUniqueId())) {
            playerDropItemEvent.setCancelled(true);
        }
    }
}
