package org.huanshi.mc.lib.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Listener;
import org.huanshi.mc.lib.service.SkillService;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Listener
public class PlayerMoveListener extends AbstractListener {
    @Autowired
    private SkillService skillService;

    @EventHandler
    public void onPlayerMove(@NotNull PlayerMoveEvent playerMoveEvent) {
        UUID uuid = playerMoveEvent.getPlayer().getUniqueId();
        if (skillService.isRunning(uuid) || skillService.isRunning(uuid)) {
            playerMoveEvent.setCancelled(true);
        } else if (skillService.isRunning(uuid) && playerMoveEvent.hasChangedPosition()) {
            playerMoveEvent.setCancelled(true);
        }
    }
}
