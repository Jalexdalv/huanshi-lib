package org.huanshi.mc.lib.listener;

import org.bukkit.event.EventHandler;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.command.AbstractPlayerCommand;
import org.huanshi.mc.framework.event.ComponentCreateEvent;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.huanshi.mc.lib.service.CommandService;
import org.jetbrains.annotations.NotNull;

public class ComponentCreateListener extends AbstractListener {
    @Autowired
    private CommandService commandService;

    @EventHandler
    public final void onComponentCreate(@NotNull final ComponentCreateEvent componentCreateEvent) {
        if (componentCreateEvent.getComponent() instanceof final AbstractPlayerCommand playerCommand) {
            if (playerCommand.isOp()) {
                commandService.addCommandHead(playerCommand.getHead());
            } else {
                commandService.addOpCommandHead(playerCommand.getHead());
            }
        }
    }
}
