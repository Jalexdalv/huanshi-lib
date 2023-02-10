package org.huanshi.mc.lib.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.huanshi.mc.lib.annotation.Listener;
import org.huanshi.mc.lib.utils.StatusUtils;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * PlayerMoveEvent 事件监听器
 * @author Jalexdalv
 */
@Listener
public class PlayerMoveListener extends AbstractListener {
    /**
     * PlayerMoveEvent 事件发生时处理
     * @param playerMoveEvent PlayerMoveEvent 事件
     */
    @EventHandler
    public void onPlayerMove(@NotNull PlayerMoveEvent playerMoveEvent) {
        UUID uuid = playerMoveEvent.getPlayer().getUniqueId();
        if (StatusUtils.isStunning(uuid) || StatusUtils.isStiffing(uuid)) {
            playerMoveEvent.setCancelled(true);
        } else if (StatusUtils.isRooting(uuid) && playerMoveEvent.hasChangedPosition()) {
            playerMoveEvent.setCancelled(true);
        }
    }
}
