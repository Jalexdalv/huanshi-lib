package org.huanshi.mc.lib.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.huanshi.mc.lib.service.PlayerCombatService;
import org.jetbrains.annotations.NotNull;

public class EntityDamageByEntityListener extends AbstractListener {
    @Autowired
    private PlayerCombatService playerCombatService;

    @EventHandler
    public void onEntityDamageByEntity(@NotNull EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (entityDamageByEntityEvent.getEntity() instanceof Player player) {
            playerCombatService.start(player);
        }
        if (entityDamageByEntityEvent.getDamager() instanceof Player player) {
            playerCombatService.start(player);
        }
    }
}
