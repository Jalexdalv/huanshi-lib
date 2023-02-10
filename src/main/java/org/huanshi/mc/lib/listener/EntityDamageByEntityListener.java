package org.huanshi.mc.lib.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Listener;
import org.huanshi.mc.lib.service.CombatService;
import org.jetbrains.annotations.NotNull;

/**
 * EntityDamageByEntityEvent 事件监听器
 * @author Jalexdalv
 */
@Listener
public class EntityDamageByEntityListener extends AbstractListener {
    @Autowired
    private CombatService combatService;

    /**
     * EntityDamageByEntityEvent 事件发生时处理
     * @param entityDamageByEntityEvent EntityDamageByEntityEvent 事件
     */
    @EventHandler
    public void onEntityDamageByEntity(@NotNull EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (entityDamageByEntityEvent.getEntity() instanceof Player player) {
            combatService.enter(player);
        }
        if (entityDamageByEntityEvent.getDamager() instanceof Player player) {
            combatService.enter(player);
        }
    }
}
