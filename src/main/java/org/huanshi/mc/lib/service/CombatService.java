package org.huanshi.mc.lib.service;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.api.BukkitApi;
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

    public void start(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        timer.reentry(plugin, player, true, duration, 0L, 500L, null,
            () -> {
                BukkitApi.runTask(plugin, () -> BukkitApi.callEvent(new PlayerToggleCombatEvent(player, true)));
                bossBarMap.putIfAbsent(uuid, BossBar.bossBar(Component.empty(), 1.0F, BossBar.Color.RED, BossBar.Overlay.PROGRESS));
                return true;
            }, durationLeft -> {
                player.showBossBar(bossBarMap.get(uuid).name(Zh.combat(durationLeft)).progress((float) durationLeft / (float) duration));
                return true;
            }, () -> {
                BukkitApi.runTask(plugin, () -> BukkitApi.callEvent(new PlayerToggleCombatEvent(player, false)));
                player.hideBossBar(bossBarMap.get(uuid));
                return true;
            }
        );
    }

    public void stop(@NotNull UUID uuid) {
        timer.stop(uuid);
    }

    public void stop(@NotNull Player player) {
        timer.stop(player);
    }

    public boolean isRunning(@NotNull UUID uuid) {
        return timer.isRunning(uuid);
    }

    public boolean isRunning(@NotNull Player player) {
        return timer.isRunning(player);
    }
}
