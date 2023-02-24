package org.huanshi.mc.lib.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.listener.HuanshiListener;
import org.huanshi.mc.lib.protocol.ClientCommandSender;
import org.huanshi.mc.lib.service.CombatService;
import org.jetbrains.annotations.NotNull;

public class PlayerDeathListener extends HuanshiListener {
    @Autowired
    private CombatService combatService;
    @Autowired
    private ClientCommandSender clientCommandSender;

    @EventHandler
    public void onPlayerDeath(@NotNull PlayerDeathEvent playerDeathEvent) {
        Player player = playerDeathEvent.getPlayer();
        combatService.start(player);
        clientCommandSender.autoRespawn(player);
    }
}
