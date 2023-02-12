package org.huanshi.mc.lib.service;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.huanshi.mc.lib.Plugin;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Service;
import org.huanshi.mc.lib.lang.Zh;
import org.huanshi.mc.lib.timer.Timer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InvincibleService extends AbstractService {
    @Autowired
    private Plugin plugin;
    @Autowired(file = "config.yml", path = "invincible.period")
    private int period;
    private final Map<UUID, BossBar> bossBarMap = new ConcurrentHashMap<>();
    private final Timer timer = new Timer();

    public void run(@NotNull Player player, long duration) {
        UUID uuid = player.getUniqueId();
        timer.run(plugin, uuid, true, true, duration, 0, period, null,
            () -> {
                bossBarMap.putIfAbsent(uuid, BossBar.bossBar(Component.empty(), 1.0F, BossBar.Color.WHITE, BossBar.Overlay.NOTCHED_6));
                return true;
            }, remainDuration -> player.showBossBar(bossBarMap.get(uuid).name(Zh.invincible(remainDuration)).progress((float) remainDuration / (float) duration)),
            () -> {
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
