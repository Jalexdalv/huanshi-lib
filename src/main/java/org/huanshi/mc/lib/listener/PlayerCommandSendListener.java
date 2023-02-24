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
    public void onPlayerCommandSend(@NotNull PlayerCommandSendEvent playerCommandSendEvent) {
        Collection<String> commands = playerCommandSendEvent.getCommands();
        commands.clear();
        commands.addAll(commandService.getPlayerCommandNames(playerCommandSendEvent.getPlayer()));
    }
}
