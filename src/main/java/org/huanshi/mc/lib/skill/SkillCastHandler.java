package org.huanshi.mc.lib.skill;

/**
 * 技能释放时处理
 * @author Jalexdalv
 */
public interface SkillCastHandler {
    /**
     * 处理
     * @return 是否执行
     */
    boolean handle();
}
