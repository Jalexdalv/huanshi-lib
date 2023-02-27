package org.huanshi.mc.lib.service;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.huanshi.mc.framework.utils.FormatUtils;
import org.huanshi.mc.lib.Plugin;
import org.huanshi.mc.lib.config.MainConfig;
import org.huanshi.mc.lib.event.PlayerToggleCombatEvent;
import org.huanshi.mc.lib.lang.Zh;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerCombatService extends AbstractBossBarService {
    @Autowired
    private Plugin plugin;
    @Autowired
    private MainConfig mainConfig;
    @Autowired
    private Zh zh;
    private long duration;
    private Component combat;

    @Override
    public void onLoad(@NotNull AbstractPlugin plugin) {
        duration = mainConfig.getLong("combat.duration");
        combat = zh.getComponent("combat");
    }

    public void start(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        getTimerHelper().start(uuid, true, false, duration, 0L, 500L, null,
            () -> {
                BukkitAPI.runTask(plugin, () -> BukkitAPI.callEvent(new PlayerToggleCombatEvent(player, true)));
                getBossBarMap().putIfAbsent(uuid, BossBar.bossBar(Component.empty(), 1.0F, BossBar.Color.RED, BossBar.Overlay.PROGRESS));
                return true;
            }, durationLeft -> {
                player.showBossBar(getBossBarMap().get(uuid).name(zh.format(combat, FormatUtils.convertMillisecondToSecond(durationLeft))).progress((float) durationLeft / (float) duration));
                return true;
            }, () -> {
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new PlayerToggleCombatEvent(player, false)));
                player.hideBossBar(getBossBarMap().get(uuid));
                return true;
            }
        );
    }
}
