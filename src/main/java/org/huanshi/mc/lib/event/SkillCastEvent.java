package org.huanshi.mc.lib.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.huanshi.mc.lib.skill.AbstractSkill;
import org.jetbrains.annotations.NotNull;

public class SkillCastEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Player player;
    private final AbstractSkill skill;

    public SkillCastEvent(@NotNull Player player, @NotNull AbstractSkill skill) {
        this.player = player;
        this.skill = skill;
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

    public @NotNull AbstractSkill getSkill() {
        return skill;
    }
}
