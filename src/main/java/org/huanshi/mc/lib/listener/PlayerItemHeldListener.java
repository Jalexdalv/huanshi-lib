package org.huanshi.mc.lib.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Listener;
import org.huanshi.mc.lib.service.SkillService;
import org.jetbrains.annotations.NotNull;

@Listener
public class PlayerItemHeldListener extends AbstractListener {
    @Autowired
    private SkillService skillService;

    @EventHandler
    public void onPlayerItemHeld(@NotNull PlayerItemHeldEvent playerItemHeldEvent) {
        if (skillService.isCasting(playerItemHeldEvent.getPlayer().getUniqueId())) {
            playerItemHeldEvent.setCancelled(true);
        }
    }
}
