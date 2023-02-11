package org.huanshi.mc.lib.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Listener;
import org.huanshi.mc.lib.service.RootService;
import org.huanshi.mc.lib.service.StiffService;
import org.huanshi.mc.lib.service.StunService;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * PlayerMoveEvent 事件监听器
 * @author Jalexdalv
 */
@Listener
public class PlayerMoveListener extends AbstractListener {
    @Autowired
    private StunService stunService;
    @Autowired
    private RootService rootService;
    @Autowired
    private StiffService stiffService;

    /**
     * PlayerMoveEvent 事件发生时处理
     * @param playerMoveEvent PlayerMoveEvent 事件
     */
    @EventHandler
    public void onPlayerMove(@NotNull PlayerMoveEvent playerMoveEvent) {
        UUID uuid = playerMoveEvent.getPlayer().getUniqueId();
        if (stunService.isRunning(uuid) || stiffService.isRunning(uuid)) {
            playerMoveEvent.setCancelled(true);
        } else if (rootService.isRunning(uuid) && playerMoveEvent.hasChangedPosition()) {
            playerMoveEvent.setCancelled(true);
        }
    }
}
