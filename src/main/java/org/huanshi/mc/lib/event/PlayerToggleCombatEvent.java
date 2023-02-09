package org.huanshi.mc.lib.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerToggleCombatEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Player player;
    private final boolean isCombating;

    public PlayerToggleCombatEvent(@NotNull Player player, boolean isCombating) {
        this.player = player;
        this.isCombating = isCombating;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    public boolean isCombating() {
        return isCombating;
    }
}
