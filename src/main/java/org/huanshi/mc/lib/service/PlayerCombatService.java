package org.huanshi.mc.lib.service;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.huanshi.mc.framework.service.AbstractService;
import org.huanshi.mc.framework.timer.TimerHelper;
import org.huanshi.mc.framework.utils.FormatUtils;
import org.huanshi.mc.lib.Plugin;
import org.huanshi.mc.lib.config.MainConfig;
import org.huanshi.mc.lib.event.PlayerToggleCombatEvent;
import org.huanshi.mc.lib.lang.Zh;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerCombatService extends AbstractService {
    @Autowired
    private Plugin plugin;
    @Autowired
    private MainConfig mainConfig;
    @Autowired
    private Zh zh;
    private long duration;
    private Component combat;
    private TimerHelper timerHelper;
    private final Map<UUID, BossBar> bossBarMap = new ConcurrentHashMap<>();

    @Override
    public void onLoad(@NotNull AbstractPlugin plugin) {
        duration = mainConfig.getLong("combat.duration");
        combat = zh.getComponent("combat");
        timerHelper = new TimerHelper(plugin);
    }

    public void start(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        timerHelper.start(uuid, true, false, duration, 0L, 500L, null,
            () -> {
                BukkitAPI.runTask(plugin, () -> BukkitAPI.callEvent(new PlayerToggleCombatEvent(player, true)));
                bossBarMap.putIfAbsent(uuid, BossBar.bossBar(Component.empty(), 1.0F, BossBar.Color.RED, BossBar.Overlay.PROGRESS));
                return true;
            }, durationLeft -> {
                player.showBossBar(bossBarMap.get(uuid).name(zh.format(combat, FormatUtils.convertMillisecondToSecond(durationLeft))).progress((float) durationLeft / (float) duration));
                return true;
            }, () -> {
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new PlayerToggleCombatEvent(player, false)));
                player.hideBossBar(bossBarMap.get(uuid));
                return true;
            }
        );
    }

    public void stop(@NotNull UUID uuid) {
        timerHelper.stop(uuid);
    }

    public void stop(@NotNull Player player) {
        stop(player.getUniqueId());
    }

    public boolean isRunning(@NotNull UUID uuid) {
        return timerHelper.isRunning(uuid);
    }

    public boolean isRunning(@NotNull Player player) {
        return isRunning(player.getUniqueId());
    }
}
