package org.huanshi.mc.lib.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.huanshi.mc.lib.skill.AbstractSkill;
import org.jetbrains.annotations.NotNull;

/**
 * 技能释放事件
 * @author Jalexdalv
 */
public class SkillCastEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Player player;
    private final AbstractSkill skill;

    /**
     * 构造函数
     * @param player 玩家
     * @param skill 技能
     */
    public SkillCastEvent(@NotNull Player player, @NotNull AbstractSkill skill) {
        this.player = player;
        this.skill = skill;
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
     * 获取技能
     * @return 技能
     */
    public @NotNull AbstractSkill getSkill() {
        return skill;
    }
}
