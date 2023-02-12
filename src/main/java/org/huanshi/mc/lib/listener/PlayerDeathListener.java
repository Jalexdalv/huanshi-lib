package org.huanshi.mc.lib.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Listener;
import org.huanshi.mc.lib.protocol.CommandsSender;
import org.huanshi.mc.lib.service.CombatService;
import org.jetbrains.annotations.NotNull;

@Listener
public class PlayerDeathListener extends AbstractListener {
    @Autowired
    private CombatService combatService;
    @Autowired
    private CommandsSender commandsSender;

    @EventHandler
    public void onPlayerDeath(@NotNull PlayerDeathEvent playerDeathEvent) {
        Player player = playerDeathEvent.getPlayer();
        combatService.run(player);
        commandsSender.autoRespawn(player);
    }
}
