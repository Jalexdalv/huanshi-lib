package org.huanshi.mc.lib.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.huanshi.mc.lib.annotation.Data;
import org.huanshi.mc.lib.annotation.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Listener
public class PlayerCommandSendListener extends AbstractListener {
    @Data(name = "COMMAND_SET")
    private Set<String> commandSet;
    @Data(name = "OP_COMMAND_SET")
    private Set<String> opCommandSet;

    @EventHandler
    public void onPlayerCommandSend(@NotNull PlayerCommandSendEvent playerCommandSendEvent) {
//        Collection<String> collection = playerCommandSendEvent.getCommands();
//        collection.clear();
//        collection.addAll(commandSet);
//        if (playerCommandSendEvent.getPlayer().isOp()) {
//            collection.addAll(opCommandSet);
//        }
    }
}
