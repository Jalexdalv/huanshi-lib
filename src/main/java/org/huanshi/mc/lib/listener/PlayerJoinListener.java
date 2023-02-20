package org.huanshi.mc.lib.listener;

import net.kyori.adventure.util.TriState;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.huanshi.mc.lib.Plugin;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinListener extends AbstractListener {
    @Autowired
    private Plugin plugin;

    @EventHandler
    public final void onPlayerJoin(@NotNull final PlayerJoinEvent playerJoinEvent) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> playerJoinEvent.getPlayer().setFlyingFallDamage(TriState.FALSE), 5L);
    }
}
