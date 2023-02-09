package org.huanshi.mc.lib.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Listener;
import org.huanshi.mc.lib.protocol.CommandSender;
import org.huanshi.mc.lib.service.CombatService;
import org.jetbrains.annotations.NotNull;

@Listener
public class PlayerDeathListener extends AbstractListener {
    @Autowired
    private CommandSender commandSender;
    @Autowired
    private CombatService combatService;

    @EventHandler
    public void onPlayerDeath(@NotNull PlayerDeathEvent playerDeathEvent) {
        Player player = playerDeathEvent.getPlayer();
        combatService.exit(player.getUniqueId());
        commandSender.respawn(player);
    }
}
