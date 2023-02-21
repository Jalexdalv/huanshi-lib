package org.huanshi.mc.lib.listener;

import org.bukkit.event.EventHandler;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.command.AbstractPlayerCommand;
import org.huanshi.mc.framework.engine.Component;
import org.huanshi.mc.framework.event.ComponentScanCompleteEvent;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.huanshi.mc.lib.service.CommandNameService;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ComponentScanCompleteListener extends AbstractListener {
    @Autowired
    private CommandNameService commandNameService;

    @EventHandler
    public void onComponentCreate(@NotNull ComponentScanCompleteEvent componentScanCompleteEvent) {
        for (Map.Entry<Class<? extends Component>, Component> entry : componentScanCompleteEvent.getComponentMap().entrySet()) {
            if (AbstractPlayerCommand.class.isAssignableFrom(entry.getKey())) {
                AbstractPlayerCommand playerCommand = (AbstractPlayerCommand) entry.getValue();
                if (playerCommand.isOp()) {
                    commandNameService.addName(playerCommand.getName());
                } else {
                    commandNameService.addOpName(playerCommand.getName());
                }
            }
        }
    }
}
