package org.huanshi.mc.lib.listener;

import org.bukkit.event.EventHandler;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.command.HuanshiCommand;
import org.huanshi.mc.framework.event.ComponentScanCompleteEvent;
import org.huanshi.mc.framework.listener.HuanshiListener;
import org.huanshi.mc.framework.pojo.HuanshiComponent;
import org.huanshi.mc.lib.service.CommandService;
import org.jetbrains.annotations.NotNull;

public class ComponentScanCompleteListener extends HuanshiListener {
    @Autowired
    private CommandService commandService;

    @EventHandler
    public void onComponentScanComplete(@NotNull ComponentScanCompleteEvent componentScanCompleteEvent) {
        for (HuanshiComponent huanshiComponent : componentScanCompleteEvent.getHuanshiComponents()) {
            if (huanshiComponent instanceof HuanshiCommand huanshiCommand) {
                commandService.register(huanshiCommand.getName(), huanshiCommand);
            }
        }
    }
}
