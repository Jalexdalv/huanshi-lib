package org.huanshi.mc.lib.service;

import org.huanshi.mc.lib.annotation.Service;
import org.huanshi.mc.lib.skill.AbstractSkill;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SkillService extends AbstractService {
    private final Map<UUID, AbstractSkill> castMap = new HashMap<>();

    /**
     * 释放
     * @param uuid UUID
     */
    public void cast(@NotNull UUID uuid, @NotNull AbstractSkill skill) {
        castMap.put(uuid, skill);
    }

    /**
     * 结束
     * @param uuid UUID
     */
    public void finish(@NotNull UUID uuid) {
        castMap.remove(uuid);
    }

    /**
     * 判断是否正在释放
     * @param uuid UUID
     * @return 是否正在释放
     */
    public boolean isCasting(@NotNull UUID uuid) {
        return castMap.containsKey(uuid);
    }

    /**
     * 判断是否正在释放
     * @param uuid UUID
     * @param skill 技能
     * @return 是否正在释放
     */
    public boolean isCasting(@NotNull UUID uuid, @NotNull AbstractSkill skill) {
        return castMap.get(uuid) == skill;
    }
}
