package org.huanshi.mc.lib.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 玩家切换战斗状态事件
 * @author Jalexdalv
 */
public class PlayerToggleCombatEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Player player;
    private final boolean combat;

    /**
     * 构造函数
     * @param player 玩家
     * @param combat 是否处于战斗状态
     */
    public PlayerToggleCombatEvent(@NotNull Player player, boolean combat) {
        this.player = player;
        this.combat = combat;
    }

    /**
     * 获取处理列表
     * @return 处理列表
     */
    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * 获取处理列表
     * @return 处理列表
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    /**
     * 获取玩家
     * @return 玩家
     */
    public @NotNull Player getPlayer() {
        return player;
    }

    /**
     * 判断是否处于战斗状态
     * @return 是否处于战斗状态
     */
    public boolean isCombating() {
        return combat;
    }
}
