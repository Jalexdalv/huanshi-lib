package org.huanshi.mc.lib.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.huanshi.mc.lib.Plugin;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@Listener
public class PlayerCommandSendListener extends AbstractListener {
    @Autowired
    private Plugin plugin;

    @EventHandler
    public void onPlayerCommandSend(@NotNull PlayerCommandSendEvent playerCommandSendEvent) {
        if (!playerCommandSendEvent.getPlayer().isOp()) {
            Collection<String> commandCollection = playerCommandSendEvent.getCommands();
            commandCollection.clear();
            commandCollection.addAll(plugin.getCommands());
        }
    }
}
