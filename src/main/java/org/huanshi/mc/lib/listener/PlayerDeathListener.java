package org.huanshi.mc.lib.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.huanshi.mc.lib.service.PlayerCombatService;
import org.jetbrains.annotations.NotNull;

public class PlayerDeathListener extends AbstractListener {
    @Autowired
    private PlayerCombatService playerCombatService;

    @EventHandler
    public void onPlayerDeath(@NotNull PlayerDeathEvent playerDeathEvent) {
        Player player = playerDeathEvent.getPlayer();
        player.spigot().respawn();
        playerCombatService.stop(player);
    }
}
