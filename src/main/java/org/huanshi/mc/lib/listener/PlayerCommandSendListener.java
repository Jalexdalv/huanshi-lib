package org.huanshi.mc.lib.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Listener;
import org.huanshi.mc.lib.manager.CommandManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@Listener
public class PlayerCommandSendListener extends AbstractListener {
    @Autowired
    private CommandManager commandManager;

    @EventHandler
    public void onPlayerCommandSend(@NotNull PlayerCommandSendEvent playerCommandSendEvent) {
        Collection<String> collection = playerCommandSendEvent.getCommands();
        collection.clear();
        collection.addAll(commandManager.getCommands());
        if (playerCommandSendEvent.getPlayer().isOp()) {
            collection.addAll(commandManager.getOpCommands());
        }
    }
}
