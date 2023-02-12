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

/**
 * 战斗状态服务
 * @author Jalexdalv
 */
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

    /**
     * 进入
     * @param player 玩家
     */
    public void enter(@NotNull Player player) {
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

    /**
     * 离开
     * @param uuid UUID
     */
    public void exit(@NotNull UUID uuid) {
        timer.clear(uuid);
    }

    /**
     * 判断是否处于战斗状态
     * @param uuid UUID
     * @return 是否处于战斗状态
     */
    public boolean isCombating(@NotNull UUID uuid) {
        return timer.isRunning(uuid);
    }
}
