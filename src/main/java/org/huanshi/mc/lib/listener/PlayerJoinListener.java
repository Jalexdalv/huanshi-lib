package org.huanshi.mc.lib.listener;

import net.kyori.adventure.util.TriState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.huanshi.mc.lib.annotation.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * PlayerJoinEvent 事件监听器
 * @author Jalexdalv
 */
@Listener
public class PlayerJoinListener extends AbstractListener {
    /**
     * PlayerJoinEvent 事件发生时处理
     * @param playerJoinEvent PlayerJoinEvent 事件
     */
    @EventHandler
    public void onPlayerJoinEvent(@NotNull PlayerJoinEvent playerJoinEvent) {
        Player player = playerJoinEvent.getPlayer();
        player.setFlyingFallDamage(TriState.FALSE);
        player.setCollidable(true);
    }
}
