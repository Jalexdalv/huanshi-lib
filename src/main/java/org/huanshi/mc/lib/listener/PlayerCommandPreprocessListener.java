package org.huanshi.mc.lib.listener;

import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.huanshi.mc.framework.AbstractPlugin;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.huanshi.mc.framework.timer.CoolDownHelper;
import org.huanshi.mc.lib.config.MainConfig;
import org.huanshi.mc.lib.lang.Zh;
import org.jetbrains.annotations.NotNull;

public class PlayerCommandPreprocessListener extends AbstractListener {
    @Autowired
    private MainConfig mainConfig;
    @Autowired
    private Zh zh;
    private long cd;
    private Component unknownCommand, useCommandFast;
    private final CoolDownHelper coolDownHelper = new CoolDownHelper();

    @Override
    public void onLoad(@NotNull AbstractPlugin plugin) {
        cd = mainConfig.getLong("command.cd");
        unknownCommand = zh.getComponent("unknown-command");
        useCommandFast = zh.getComponent("use-command-fast");
    }

    @EventHandler
    public void onPlayerCommandPreprocess(@NotNull PlayerCommandPreprocessEvent playerCommandPreprocessEvent) {
        Player player = playerCommandPreprocessEvent.getPlayer();
        coolDownHelper.start(player.getUniqueId(), false, cd, null,
            () -> {
                if (BukkitAPI.getPluginCommand(StringUtils.replaceOnce(StringUtils.split(playerCommandPreprocessEvent.getMessage(), " ")[0], "/", "")) == null) {
                    playerCommandPreprocessEvent.setCancelled(true);
                    player.sendMessage(unknownCommand);
                }
                return true;
            }, durationLeft -> {
                playerCommandPreprocessEvent.setCancelled(true);
                player.sendMessage(useCommandFast);
            }
        );
    }
}
