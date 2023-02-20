package org.huanshi.mc.lib.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.huanshi.mc.lib.protocol.ClientCommandSender;
import org.huanshi.mc.lib.service.CombatService;
import org.jetbrains.annotations.NotNull;

public class PlayerDeathListener extends AbstractListener {
    @Autowired
    private CombatService combatService;
    @Autowired
    private ClientCommandSender clientCommandSender;

    @EventHandler
    public final void onPlayerDeath(@NotNull final PlayerDeathEvent playerDeathEvent) {
        final Player player = playerDeathEvent.getPlayer();
        combatService.start(player);
        clientCommandSender.autoRespawn(player);
    }
}
