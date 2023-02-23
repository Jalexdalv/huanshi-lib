package org.huanshi.mc.lib.service;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.huanshi.mc.framework.service.AbstractService;
import org.huanshi.mc.framework.timer.Timer;
import org.huanshi.mc.framework.utils.FormatUtils;
import org.huanshi.mc.lib.Plugin;
import org.huanshi.mc.lib.config.MainConfig;
import org.huanshi.mc.lib.event.PlayerToggleCombatEvent;
import org.huanshi.mc.lib.lang.Zh;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

public class CombatService extends AbstractService {
    @Autowired
    private Plugin plugin;
    @Autowired
    private MainConfig mainConfig;
    @Autowired
    private Zh zh;
    protected long duration;
    protected Component combat;
    protected final Map<UUID, BossBar> bossBarMap = new ConcurrentHashMap<>();
    protected final Map<UUID, Timer> timerMap = new WeakHashMap<>();

    @Override
    public void load(@NotNull AbstractPlugin plugin) {
        duration = mainConfig.getLong("combat.duration");
        combat = zh.getComponent("combat");
    }

    public void start(@NotNull Player player) {
        timerMap.compute(player.getUniqueId(), (uuid, timer) -> {
            if (timer == null || !timer.isRunning()) {
                return new Timer(plugin, true, true, duration, 0L, 500L) {
                    @Override
                    protected boolean onStart() {
                        BukkitAPI.runTask(plugin, () -> BukkitAPI.callEvent(new PlayerToggleCombatEvent(player, true)));
                        bossBarMap.putIfAbsent(uuid, BossBar.bossBar(Component.empty(), 1.0F, BossBar.Color.RED, BossBar.Overlay.PROGRESS));
                        return true;
                    }
                    @Override
                    protected boolean onRun(long durationLeft) {
                        player.showBossBar(bossBarMap.get(uuid).name(zh.formatComponent(combat, FormatUtils.convertMillisecondToSecond(durationLeft))).progress((float) durationLeft / (float) duration));
                        return true;
                    }
                    @Override
                    protected boolean onStop() {
                        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new PlayerToggleCombatEvent(player, false)));
                        player.hideBossBar(bossBarMap.get(uuid));
                        return true;
                    }
                };
            }
            return timer;
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
