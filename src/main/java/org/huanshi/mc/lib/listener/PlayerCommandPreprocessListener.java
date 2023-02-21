package org.huanshi.mc.lib.listener;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.huanshi.mc.framework.timer.Cooldowner;
import org.huanshi.mc.lib.config.MainConfig;
import org.huanshi.mc.lib.lang.Zh;
import org.huanshi.mc.lib.service.CommandNameService;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerCommandPreprocessListener extends AbstractListener {
    @Autowired
    private MainConfig mainConfig;
    @Autowired
    private CommandNameService commandNameService;
    private long cd;
    private final Map<UUID, Cooldowner> cooldownerMap = new HashMap<>();

    @Override
    public final void onLoad() {
        cd = mainConfig.getLong("command.cd");
    }

    @EventHandler
    public void onPlayerCommandPreprocess(@NotNull PlayerCommandPreprocessEvent playerCommandPreprocessEvent) {
        Player player = playerCommandPreprocessEvent.getPlayer();
        cooldownerMap.computeIfAbsent(player.getUniqueId(), uuid -> new Cooldowner(false, cd) {
            @Override
            protected boolean onStart() {
                if (!commandNameService.isCommand(StringUtils.replaceOnce(StringUtils.split(playerCommandPreprocessEvent.getMessage(), " ")[0], "/", ""))) {
                    playerCommandPreprocessEvent.setCancelled(true);
                    player.sendMessage(Zh.UNKNOWN_COMMAND);
                }
                return true;
            }

            @Override
            protected void onRun(long durationLeft) {
                playerCommandPreprocessEvent.setCancelled(true);
                player.sendMessage(Zh.USE_COMMAND_FAST);
            }
        }).start();
    }
}
