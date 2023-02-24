package org.huanshi.mc.lib.service;

import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.command.AbstractCommand;
import org.huanshi.mc.framework.service.AbstractService;
import org.huanshi.mc.framework.timer.Cooldowner;
import org.huanshi.mc.lib.config.MainConfig;
import org.huanshi.mc.lib.lang.Zh;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CommandService extends AbstractService {
    @Autowired
    private MainConfig mainConfig;
    @Autowired
    private Zh zh;
    protected long cd;
    protected Component unknownCommand, useCommandFast;
    protected final Map<String, AbstractCommand> commandMap = new HashMap<>();
    protected final Map<UUID, Cooldowner> cooldownerMap = new HashMap<>();

    @Override
    public void onLoad(@NotNull AbstractPlugin plugin) {
        cd = mainConfig.getLong("command.cd");
        unknownCommand = zh.getComponent("unknown-command");
        useCommandFast = zh.getComponent("use-command-fast");
    }

    public boolean check(@NotNull Player player, @NotNull String message) {
        return cooldownerMap.compute(player.getUniqueId(), (uuid, cooldowner) -> {
            if (cooldowner == null) {
                return new Cooldowner(false, cd) {
                    @Override
                    protected boolean onStart() {
                        if (check(StringUtils.replaceOnce(StringUtils.split(message, " ")[0], "/", ""))) {
                            return true;
                        }
                        player.sendMessage(unknownCommand);
                        return false;
                    }
                    @Override
                    protected boolean onRun(long durationLeft) {
                        player.sendMessage(useCommandFast);
                        return false;
                    }
                };
            }
            return cooldowner;
        }).start();
    }

    public void register(@NotNull String name, @NotNull AbstractCommand command) {
        commandMap.put(name, command);
    }

    public boolean check(@NotNull String name) {
        return commandMap.containsKey(name);
    }

    public @NotNull Collection<String> getCommandNames(@NotNull Player player) {
        if (player.isOp()) {
            return commandMap.keySet();
        }
        List<String> commandNames = new LinkedList<>();
        for (AbstractCommand command : commandMap.values()) {
            if (command.hasPermission(player)) {
                commandNames.add(command.getName());
            }
        }
        return commandNames;
    }
}
