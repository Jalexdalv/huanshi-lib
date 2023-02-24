package org.huanshi.mc.lib.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerBlockEvent extends Event {
    protected static final HandlerList HANDLER_LIST = new HandlerList();
    @Getter
    protected final Player player;

    public PlayerBlockEvent(@NotNull Player player) {
        this.player = player;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
