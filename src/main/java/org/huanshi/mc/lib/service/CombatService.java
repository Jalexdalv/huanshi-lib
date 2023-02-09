package org.huanshi.mc.lib.service;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.huanshi.mc.lib.Plugin;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Service;
import org.huanshi.mc.lib.event.PlayerToggleCombatEvent;
import org.huanshi.mc.lib.lang.Zh;
import org.huanshi.mc.lib.timer.AsyncTimer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CombatService extends AbstractService {
    @Autowired
    private Plugin plugin;
    @Autowired(file = "config.yml", path = "combat.time")
    private int time;
    private final Map<UUID, BossBar> bossBarMap = new ConcurrentHashMap<>();
    private final AsyncTimer asyncTimer = new AsyncTimer();

    public void enter(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        asyncTimer.run(uuid, plugin, time, 20L, true, null,
            () -> {
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new PlayerToggleCombatEvent(player, true)));
                bossBarMap.putIfAbsent(uuid, BossBar.bossBar(Zh.combat(time), 1F, BossBar.Color.RED, BossBar.Overlay.PROGRESS));
                return true;
            },
            restTime -> {
                player.showBossBar(bossBarMap.get(uuid).name(Zh.combat(restTime)).progress((float) restTime / (float) time));
                return true;
            },
            () -> {
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new PlayerToggleCombatEvent(player, false)));
                player.hideBossBar(bossBarMap.get(uuid));
                return true;
            }
        );
    }

    public void exit(@NotNull UUID uuid) {
        asyncTimer.clear(uuid);
    }

    public boolean isCombating(@NotNull UUID uuid) {
        return asyncTimer.isRunning(uuid);
    }
}
