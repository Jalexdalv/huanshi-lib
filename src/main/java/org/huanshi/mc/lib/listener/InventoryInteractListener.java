package org.huanshi.mc.lib.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Listener;
import org.huanshi.mc.lib.service.SkillService;
import org.jetbrains.annotations.NotNull;

@Listener
public class InventoryInteractListener extends AbstractListener {
    @Autowired
    private SkillService skillService;

    @EventHandler
    public void onInventoryInteract(@NotNull InventoryInteractEvent inventoryInteractEvent) {
        if (inventoryInteractEvent.getWhoClicked() instanceof Player player && inventoryInteractEvent.getInventory() == player.getInventory() && skillService.isRunning(player.getUniqueId())) {
            inventoryInteractEvent.setCancelled(true);
        }
    }
}
