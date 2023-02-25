package org.huanshi.mc.lib.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.jetbrains.annotations.NotNull;

public class PlayerCommandSendListener extends AbstractListener {
    @EventHandler
    public void onPlayerCommandSend(@NotNull PlayerCommandSendEvent playerCommandSendEvent) {
        playerCommandSendEvent.getCommands().removeIf(command -> BukkitAPI.getPluginCommand(command) == null);
    }
}
