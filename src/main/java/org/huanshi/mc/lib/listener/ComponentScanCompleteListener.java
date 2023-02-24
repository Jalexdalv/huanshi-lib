package org.huanshi.mc.lib.listener;

import org.bukkit.event.EventHandler;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.command.AbstractCommand;
import org.huanshi.mc.framework.event.ComponentScanCompleteEvent;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.huanshi.mc.framework.pojo.HuanshiComponent;
import org.huanshi.mc.lib.service.CommandService;
import org.jetbrains.annotations.NotNull;

public class ComponentScanCompleteListener extends AbstractListener {
    @Autowired
    private CommandService commandService;

    @EventHandler
    public void onComponentScanComplete(@NotNull ComponentScanCompleteEvent componentScanCompleteEvent) {
        for (HuanshiComponent huanshiComponent : componentScanCompleteEvent.getHuanshiComponents()) {
            if (huanshiComponent instanceof AbstractCommand command) {
                commandService.register(command.getName(), command);
            }
        }
    }
}
