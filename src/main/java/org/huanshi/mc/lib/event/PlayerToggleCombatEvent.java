package org.huanshi.mc.lib.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerToggleCombatEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    @Getter
    private final Player player;
    @Getter
    private final boolean combating;

    public PlayerToggleCombatEvent(@NotNull Player player, boolean combating) {
        this.player = player;
        this.combating = combating;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
