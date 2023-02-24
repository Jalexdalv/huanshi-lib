package org.huanshi.mc.lib.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.huanshi.mc.framework.command.AbstractCommand;
import org.huanshi.mc.framework.event.ComponentScanCompleteEvent;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.huanshi.mc.framework.pojo.HuanshiComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PlayerCommandChecker extends AbstractListener {
    protected final Map<String, AbstractCommand> commonCommandMap = new HashMap<>();

    @EventHandler
    public void onComponentScanComplete(@NotNull ComponentScanCompleteEvent componentScanCompleteEvent) {
        for (HuanshiComponent huanshiComponent : componentScanCompleteEvent.getHuanshiComponents()) {
            if (huanshiComponent instanceof AbstractCommand command) {
                commonCommandMap.put(command.getName(), command);
            }
        }
    }

    public boolean check(@NotNull String name) {
        return commonCommandMap.containsKey(name);
    }

    public @NotNull Collection<String> getPlayerCommandNames(@NotNull Player player) {
        if (player.isOp()) {
            return commonCommandMap.keySet();
        }
        List<String> commandNames = new LinkedList<>();
        for (AbstractCommand command : commonCommandMap.values()) {
            if (command.hasPermission(player)) {
                commandNames.add(command.getName());
            }
        }
        return commandNames;
    }
}
