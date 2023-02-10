package org.huanshi.mc.lib.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.huanshi.mc.lib.annotation.Listener;
import org.huanshi.mc.lib.event.PlayerToggleCombatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * PlayerToggleCombatEvent 事件监听器
 * @author Jalexdalv
 */
@Listener
public class PlayerToggleCombatListener extends AbstractListener {
    private final Set<UUID> flySet = new HashSet<>();

    /**
     * PlayerToggleCombatEvent 事件发生时处理
     * @param playerToggleCombatEvent PlayerToggleCombatEvent 事件
     */
    @EventHandler
    public void onPlayerToggleCombat(@NotNull PlayerToggleCombatEvent playerToggleCombatEvent) {
        Player player = playerToggleCombatEvent.getPlayer();
        UUID uuid = player.getUniqueId();
        if (playerToggleCombatEvent.isCombating()) {
            if (player.getAllowFlight()) {
                flySet.add(uuid);
                player.setAllowFlight(false);
            }
        } else if (flySet.contains(uuid)) {
            player.setAllowFlight(true);
            flySet.remove(uuid);
        }
    }
}
