package org.huanshi.mc.lib.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Listener;
import org.huanshi.mc.lib.service.SkillService;
import org.jetbrains.annotations.NotNull;

/**
 * PlayerSwapHandItemsEvent 事件监听器
 * @author Jalexdalv
 */
@Listener
public class PlayerSwapHandItemsListener extends AbstractListener {
    @Autowired
    private SkillService skillService;

    /**
     * PlayerSwapHandItemsEvent 事件发生时处理
     * @param playerSwapHandItemsEvent PlayerSwapHandItemsEvent 事件
     */
    @EventHandler
    public void onPlayerSwapHandItems(@NotNull PlayerSwapHandItemsEvent playerSwapHandItemsEvent) {
        if (skillService.isCasting(playerSwapHandItemsEvent.getPlayer().getUniqueId())) {
            playerSwapHandItemsEvent.setCancelled(true);
        }
    }
}
