package org.huanshi.mc.lib.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.huanshi.mc.lib.protocol.ClientCommandSender;
import org.huanshi.mc.lib.service.PlayerCombatService;
import org.jetbrains.annotations.NotNull;

public class PlayerDeathListener extends AbstractListener {
    @Autowired
    private PlayerCombatService playerCombatService;
    @Autowired
    private ClientCommandSender clientCommandSender;

    @EventHandler
    public void onPlayerDeath(@NotNull PlayerDeathEvent playerDeathEvent) {
        Player player = playerDeathEvent.getPlayer();
        playerCombatService.stop(player);
        clientCommandSender.autoRespawn(player);
    }
}
