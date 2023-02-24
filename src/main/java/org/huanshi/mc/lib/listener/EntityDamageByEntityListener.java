package org.huanshi.mc.lib.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.listener.HuanshiListener;
import org.huanshi.mc.lib.service.CombatService;
import org.jetbrains.annotations.NotNull;

public class EntityDamageByEntityListener extends HuanshiListener {
    @Autowired
    private CombatService combatService;

    @EventHandler
    public void onEntityDamageByEntity(@NotNull EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (entityDamageByEntityEvent.getEntity() instanceof Player player) {
            combatService.start(player);
        }
        if (entityDamageByEntityEvent.getDamager() instanceof Player player) {
            combatService.start(player);
        }
    }
}
