package org.huanshi.mc.lib.service;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.huanshi.mc.lib.Plugin;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Service;
import org.huanshi.mc.lib.event.PlayerToggleCombatEvent;
import org.huanshi.mc.lib.lang.Zh;
import org.huanshi.mc.lib.timer.Timer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CombatService extends AbstractService {
    @Autowired
    private Plugin plugin;
    @Autowired(file = "config.yml", path = "combat.duration")
    private long duration;
    @Autowired(file = "config.yml", path = "combat.period")
    private int period;
    private final Map<UUID, BossBar> bossBarMap = new ConcurrentHashMap<>();
    private final Timer timer = new Timer();

    public void run(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        timer.run(plugin, uuid, true, true, duration, 0, period, null,
            () -> {
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new PlayerToggleCombatEvent(player, true)));
                bossBarMap.putIfAbsent(uuid, BossBar.bossBar(Component.empty(), 1.0F, BossBar.Color.RED, BossBar.Overlay.PROGRESS));
                return true;
            }, remainDuration -> player.showBossBar(bossBarMap.get(uuid).name(Zh.combat(remainDuration)).progress((float) remainDuration / (float) duration)),
            () -> {
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new PlayerToggleCombatEvent(player, false)));
                player.hideBossBar(bossBarMap.get(uuid));
                return true;
            }
        );
    }

    public void clear(@NotNull UUID uuid) {
        timer.clear(uuid);
    }

    public boolean isRunning(@NotNull UUID uuid) {
        return timer.isRunning(uuid);
    }
}
