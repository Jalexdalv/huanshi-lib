package org.huanshi.mc.lib.service;

import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.huanshi.mc.framework.HuanshiPlugin;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.command.HuanshiCommand;
import org.huanshi.mc.framework.service.HuanshiService;
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

public class CommandService extends HuanshiService {
    @Autowired
    private MainConfig mainConfig;
    @Autowired
    private Zh zh;
    protected long cd;
    protected Component unknownCommand, useCommandFast;
    protected final Map<String, HuanshiCommand> huanshiCommandMap = new HashMap<>();
    protected final Map<UUID, Cooldowner> cooldownerMap = new HashMap<>();

    @Override
    public void onLoad(@NotNull HuanshiPlugin huanshiPlugin) {
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

    public void register(@NotNull String name, @NotNull HuanshiCommand huanshiCommand) {
        huanshiCommandMap.put(name, huanshiCommand);
    }

    public boolean check(@NotNull String name) {
        return huanshiCommandMap.containsKey(name);
    }

    public @NotNull Collection<String> getHuanshiCommandNames(@NotNull Player player) {
        if (player.isOp()) {
            return huanshiCommandMap.keySet();
        }
        List<String> huanshiCommandNames = new LinkedList<>();
        for (HuanshiCommand huanshiCommand : huanshiCommandMap.values()) {
            if (huanshiCommand.hasPermission(player)) {
                huanshiCommandNames.add(huanshiCommand.getName());
            }
        }
        return huanshiCommandNames;
    }
}
