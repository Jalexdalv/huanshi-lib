package org.huanshi.mc.lib.listener;

import org.bukkit.event.EventHandler;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.command.AbstractPlayerCommand;
import org.huanshi.mc.framework.event.ComponentCreateEvent;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.huanshi.mc.lib.service.CommandHeadService;
import org.jetbrains.annotations.NotNull;

public class ComponentCreateListener extends AbstractListener {
    @Autowired
    private CommandHeadService commandHeadService;

    @EventHandler
    public void onComponentCreate(@NotNull ComponentCreateEvent componentCreateEvent) {
        if (componentCreateEvent.getComponent() instanceof AbstractPlayerCommand playerCommand) {
            if (playerCommand.isOp()) {
                commandHeadService.addCommandHead(playerCommand.getHead());
            } else {
                commandHeadService.addOpCommandHead(playerCommand.getHead());
            }
        }
    }
}
