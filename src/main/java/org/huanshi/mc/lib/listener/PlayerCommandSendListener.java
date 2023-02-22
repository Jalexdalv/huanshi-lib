package org.huanshi.mc.lib.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PlayerCommandSendListener extends AbstractListener {
    @Autowired
    private PlayerCommandChecker playerCommandChecker;

    @EventHandler
    public void onPlayerCommandSend(@NotNull PlayerCommandSendEvent playerCommandSendEvent) {
        Collection<String> commandCollection = playerCommandSendEvent.getCommands();
        commandCollection.clear();
        commandCollection.addAll(playerCommandChecker.getPlayerCommandNames(playerCommandSendEvent.getPlayer()));
    }
}
