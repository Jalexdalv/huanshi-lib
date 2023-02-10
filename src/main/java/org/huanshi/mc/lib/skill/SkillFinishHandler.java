package org.huanshi.mc.lib.skill;

/**
 * 技能结束时处理
 * @author Jalexdalv
 */
public interface SkillFinishHandler {
    /**
     * 处理
     * @return 是否执行
     */
    boolean handle();
}
