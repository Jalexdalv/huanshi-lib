package org.huanshi.mc.lib.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.huanshi.mc.lib.service.CommandService;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PlayerCommandSendListener extends AbstractListener {
    @Autowired
    private CommandService commandService;

    @EventHandler
    public final void onPlayerCommandSend(@NotNull final PlayerCommandSendEvent playerCommandSendEvent) {
        final Collection<String> collection = playerCommandSendEvent.getCommands();
        collection.clear();
        collection.addAll(commandService.getCommandHeads());
        if (playerCommandSendEvent.getPlayer().isOp()) {
            collection.addAll(commandService.getOpCommandHeads());
        }
    }
}
