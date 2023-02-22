package org.huanshi.mc.lib.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.huanshi.mc.framework.command.AbstractPlayerCommand;
import org.huanshi.mc.framework.engine.Component;
import org.huanshi.mc.framework.event.ComponentScanCompleteEvent;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PlayerCommandChecker extends AbstractListener {
    protected final List<String> playerCommandNameList = new LinkedList<>();
    protected final Map<String, AbstractPlayerCommand> commonPlayerCommandMap = new HashMap<>(), opPlayerCommandMap = new HashMap<>();

    @EventHandler
    public void onComponentScanComplete(@NotNull ComponentScanCompleteEvent componentScanCompleteEvent) {
        for (Component component : componentScanCompleteEvent.getComponents()) {
            if (component instanceof AbstractPlayerCommand playerCommand) {
                String name = playerCommand.getName();
                playerCommandNameList.add(name);
                if (playerCommand.isOp()) {
                    opPlayerCommandMap.put(name, playerCommand);
                } else {
                    commonPlayerCommandMap.put(name, playerCommand);
                }
            }
        }
    }

    public boolean check(@NotNull Player player, @NotNull String name) {
        return commonPlayerCommandMap.containsKey(name) || (player.isOp() && opPlayerCommandMap.containsKey(name));
    }

    public @NotNull Collection<String> getPlayerCommandNames(@NotNull Player player) {
        if (player.isOp()) {
            return playerCommandNameList;
        }
        List<String> playerCommandNameList = new LinkedList<>();
        for (AbstractPlayerCommand playerCommand : commonPlayerCommandMap.values()) {
            if (playerCommand.hasPermission(player)) {
                playerCommandNameList.add(playerCommand.getName());
            }
        }
        return playerCommandNameList;
    }
}
