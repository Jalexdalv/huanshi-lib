package org.huanshi.mc.lib.listener;

import net.kyori.adventure.util.TriState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.huanshi.mc.framework.listener.HuanshiListener;
import org.huanshi.mc.lib.Plugin;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinListener extends HuanshiListener {
    @Autowired
    private Plugin plugin;

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent playerJoinEvent) {
        BukkitAPI.runTaskLater(plugin, () -> playerJoinEvent.getPlayer().setFlyingFallDamage(TriState.FALSE), 100L);
    }
}
