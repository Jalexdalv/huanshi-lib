package org.huanshi.mc.lib.service;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.service.AbstractService;
import org.huanshi.mc.lib.Plugin;
import org.huanshi.mc.lib.config.MainConfig;
import org.huanshi.mc.lib.event.PlayerToggleCombatEvent;
import org.huanshi.mc.lib.lang.Zh;
import org.huanshi.mc.lib.timer.Timer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CombatService extends AbstractService {
    @Autowired
    private Plugin plugin;
    @Autowired
    private MainConfig mainConfig;
    private long duration;
    private final Map<UUID, BossBar> bossBarMap = new ConcurrentHashMap<>();
    private final Timer timer = new Timer();

    @Override
    public final void load() {
        duration = mainConfig.getLong("combat.duration");
    }

    public final void start(@NotNull final Player player) {
        final UUID uuid = player.getUniqueId();
        timer.reentry(plugin, player, true, duration, 0L, 500L, null,
            () -> {
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new PlayerToggleCombatEvent(player, true)));
                bossBarMap.putIfAbsent(uuid, BossBar.bossBar(Component.empty(), 1.0F, BossBar.Color.RED, BossBar.Overlay.PROGRESS));
                return true;
            }, durationLeft -> {
                player.showBossBar(bossBarMap.get(uuid).name(Zh.combat(durationLeft)).progress((float) durationLeft / (float) duration));
                return true;
            }, () -> {
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new PlayerToggleCombatEvent(player, false)));
                player.hideBossBar(bossBarMap.get(uuid));
                return true;
            }
        );
    }

    public final void stop(@NotNull final UUID uuid) {
        timer.stop(uuid);
    }

    public final void stop(@NotNull final Player player) {
        timer.stop(player);
    }

    public final boolean isRunning(@NotNull final UUID uuid) {
        return timer.isRunning(uuid);
    }

    public final boolean isRunning(@NotNull final Player player) {
        return timer.isRunning(player);
    }
}
