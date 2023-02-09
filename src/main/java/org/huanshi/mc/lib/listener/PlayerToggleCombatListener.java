package org.huanshi.mc.lib.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.huanshi.mc.lib.annotation.Listener;
import org.huanshi.mc.lib.event.PlayerToggleCombatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Listener
public class PlayerToggleCombatListener extends AbstractListener {
    private final Set<UUID> allowFlySet = new HashSet<>();

    @EventHandler
    public void onPlayerToggleCombat(@NotNull PlayerToggleCombatEvent playerToggleCombatEvent) {
        Player player = playerToggleCombatEvent.getPlayer();
        UUID uuid = player.getUniqueId();
        if (playerToggleCombatEvent.isCombating()) {
            if (player.getAllowFlight()) {
                allowFlySet.add(uuid);
                player.setAllowFlight(false);
            }
        } else if (allowFlySet.contains(uuid)) {
            player.setAllowFlight(true);
            allowFlySet.remove(uuid);
        }
    }
}
