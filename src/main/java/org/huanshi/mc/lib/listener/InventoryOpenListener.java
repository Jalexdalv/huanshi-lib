package org.huanshi.mc.lib.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Listener;
import org.huanshi.mc.lib.service.SkillService;
import org.jetbrains.annotations.NotNull;

@Listener
public class InventoryOpenListener extends AbstractListener {
    @Autowired
    private SkillService skillService;

    @EventHandler
    public void onInventoryOpen(@NotNull InventoryOpenEvent inventoryOpenEvent) {
        if (inventoryOpenEvent.getPlayer() instanceof Player player) {
            if (skillService.isCasting(player.getUniqueId())) {
                inventoryOpenEvent.setCancelled(true);
            }
        }
    }
}
