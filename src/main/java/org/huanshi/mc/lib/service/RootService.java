package org.huanshi.mc.lib.service;

import net.kyori.adventure.bossbar.BossBar;
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
public class RootService extends AbstractService {
    @Autowired
    private Plugin plugin;
    private final Map<UUID, BossBar> bossBarMap = new ConcurrentHashMap<>();
    private final Timer timer = new Timer();

    public void run(@NotNull Player player, double time) {
        UUID uuid = player.getUniqueId();
        timer.run(plugin, uuid, true, true, (int) Math.ceil(time * 2), 10, null,
            () -> {
                bossBarMap.putIfAbsent(uuid, BossBar.bossBar(Zh.root(time), 1.0F, BossBar.Color.PURPLE, BossBar.Overlay.NOTCHED_6));
                return true;
            }, restTime -> player.showBossBar(bossBarMap.get(uuid).name(Zh.root(restTime)).progress((float) restTime / (float) time)),
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
