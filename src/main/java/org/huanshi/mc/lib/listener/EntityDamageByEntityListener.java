package org.huanshi.mc.lib.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.huanshi.mc.lib.service.CombatService;
import org.jetbrains.annotations.NotNull;

public class EntityDamageByEntityListener extends AbstractListener {
    @Autowired
    private CombatService combatService;

    @EventHandler
    public final void onEntityDamageByEntity(@NotNull final EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (entityDamageByEntityEvent.getEntity() instanceof final Player player) {
            combatService.start(player);
        }
        if (entityDamageByEntityEvent.getDamager() instanceof final Player player) {
            combatService.start(player);
        }
    }
}
