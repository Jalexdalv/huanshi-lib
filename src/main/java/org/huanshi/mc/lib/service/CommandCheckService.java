package org.huanshi.mc.lib.service;

import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.huanshi.mc.framework.service.AbstractService;
import org.huanshi.mc.framework.timer.Cooldowner;
import org.huanshi.mc.lib.config.MainConfig;
import org.huanshi.mc.lib.lang.Zh;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommandCheckService extends AbstractService {
    @Autowired
    private MainConfig mainConfig;
    @Autowired
    private Zh zh;
    private long cd;
    private Component unknownCommand, useCommandFast;
    private final Map<UUID, Cooldowner> cooldownerMap = new HashMap<>();

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
                        if (BukkitAPI.getPluginCommand(StringUtils.replaceOnce(StringUtils.split(message, " ")[0], "/", "")) == null) {
                            player.sendMessage(unknownCommand);
                        }
                        return true;
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
}
