package org.huanshi.mc.lib.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerToggleCombatEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Player player;
    private final boolean combat;

    public PlayerToggleCombatEvent(@NotNull final Player player, final boolean combat) {
        this.player = player;
        this.combat = combat;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public final @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public final @NotNull Player getPlayer() {
        return player;
    }

    public final boolean isCombating() {
        return combat;
    }
}
