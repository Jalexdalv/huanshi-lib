package org.huanshi.mc.lib.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.huanshi.mc.lib.service.CommandHeadService;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PlayerCommandSendListener extends AbstractListener {
    @Autowired
    private CommandHeadService commandHeadService;

    @EventHandler
    public void onPlayerCommandSend(@NotNull PlayerCommandSendEvent playerCommandSendEvent) {
        Collection<String> collection = playerCommandSendEvent.getCommands();
        collection.clear();
        collection.addAll(commandHeadService.getCommandHeads());
        if (playerCommandSendEvent.getPlayer().isOp()) {
            collection.addAll(commandHeadService.getOpCommandHeads());
        }
    }
}
