package org.huanshi.mc.lib.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 玩家格挡事件
 * @author Jalexdalv
 */
public class PlayerBlockEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Player player;

    /**
     * 构造函数
     * @param player 玩家
     */
    public PlayerBlockEvent(@NotNull Player player) {
        this.player = player;
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
}
