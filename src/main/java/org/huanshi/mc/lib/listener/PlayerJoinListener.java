package org.huanshi.mc.lib.listener;

import net.kyori.adventure.util.TriState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.huanshi.mc.lib.Plugin;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Listener;
import org.jetbrains.annotations.NotNull;

@Listener
public class PlayerJoinListener extends AbstractListener {
    @Autowired
    private Plugin plugin;

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent playerJoinEvent) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Player player = playerJoinEvent.getPlayer();
            player.setFlyingFallDamage(TriState.FALSE);
            player.setCollidable(true);
        }, 5L);
    }
}
