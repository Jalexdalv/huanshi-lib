package org.huanshi.mc.lib.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.huanshi.mc.lib.event.PlayerToggleCombatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerToggleCombatListener extends AbstractListener {
    protected final Set<UUID> flySet = new HashSet<>();

    @EventHandler
    public void onPlayerToggleCombat(@NotNull PlayerToggleCombatEvent playerToggleCombatEvent) {
        Player player = playerToggleCombatEvent.getPlayer();
        UUID uuid = player.getUniqueId();
        if (playerToggleCombatEvent.isCombating()) {
            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
                flySet.add(uuid);
            }
        } else if (flySet.contains(uuid)) {
            player.setAllowFlight(true);
            flySet.remove(uuid);
        }
    }
}
