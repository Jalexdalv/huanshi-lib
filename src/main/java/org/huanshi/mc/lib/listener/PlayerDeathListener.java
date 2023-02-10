package org.huanshi.mc.lib.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Listener;
import org.huanshi.mc.lib.protocol.CommandsSender;
import org.huanshi.mc.lib.service.CombatService;
import org.jetbrains.annotations.NotNull;

/**
 * PlayerDeathEvent 事件监听器
 * @author Jalexdalv
 */
@Listener
public class PlayerDeathListener extends AbstractListener {
    @Autowired
    private CommandsSender commandsSender;
    @Autowired
    private CombatService combatService;

    /**
     * PlayerDeathEvent 事件发生时处理
     * @param playerDeathEvent PlayerDeathEvent 事件
     */
    @EventHandler
    public void onPlayerDeath(@NotNull PlayerDeathEvent playerDeathEvent) {
        Player player = playerDeathEvent.getPlayer();
        combatService.exit(player.getUniqueId());
        commandsSender.autoRespawn(player);
    }
}
