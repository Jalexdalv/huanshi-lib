package org.huanshi.mc.lib.service;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.api.BukkitApi;
import org.huanshi.mc.framework.service.AbstractService;
import org.huanshi.mc.framework.timer.Timer;
import org.huanshi.mc.lib.Plugin;
import org.huanshi.mc.lib.config.MainConfig;
import org.huanshi.mc.lib.event.PlayerToggleCombatEvent;
import org.huanshi.mc.lib.lang.Zh;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CombatService extends AbstractService {
    @Autowired
    private Plugin plugin;
    @Autowired
    private MainConfig mainConfig;
    protected long duration;
    protected final Map<UUID, BossBar> bossBarMap = new ConcurrentHashMap<>();
    protected final Map<UUID, Timer> timerMap = new HashMap<>();

    @Override
    public final void onLoad() {
        duration = mainConfig.getLong("combat.duration");
    }

    public void start(@NotNull Player player) {
        timerMap.computeIfAbsent(player.getUniqueId(), uuid -> new Timer(plugin, true, true, duration, 0L, 500L) {
            @Override
            protected boolean onStart() {
                BukkitApi.runTask(plugin, () -> BukkitApi.callEvent(new PlayerToggleCombatEvent(player, true)));
                bossBarMap.putIfAbsent(uuid, BossBar.bossBar(Component.empty(), 1.0F, BossBar.Color.RED, BossBar.Overlay.PROGRESS));
                return true;
            }
            @Override
            protected boolean onRun(long durationLeft) {
                player.showBossBar(bossBarMap.get(uuid).name(Zh.combat(durationLeft)).progress((float) durationLeft / (float) duration));
                return true;
            }
            @Override
            protected boolean onStop() {
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new PlayerToggleCombatEvent(player, false)));
                player.hideBossBar(bossBarMap.get(uuid));
                return true;
            }
        }).start();
    }

    public void stop(@NotNull UUID uuid) {
        Timer timer = timerMap.get(uuid);
        if (timer != null) {
            timer.stop();
        }
    }

    public void stop(@NotNull Player player) {
        stop(player.getUniqueId());
    }

    public boolean isRunning(@NotNull UUID uuid) {
        Timer timer = timerMap.get(uuid);
        if (timer != null) {
            return timer.isRunning();
        }
        return false;
    }

    public boolean isRunning(@NotNull Player player) {
        return isRunning(player.getUniqueId());
    }
}
