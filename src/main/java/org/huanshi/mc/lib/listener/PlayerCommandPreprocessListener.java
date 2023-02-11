package org.huanshi.mc.lib.listener;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.huanshi.mc.lib.Plugin;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Listener;
import org.huanshi.mc.lib.lang.Zh;
import org.huanshi.mc.lib.timer.CdTimer;
import org.jetbrains.annotations.NotNull;

/**
 * PlayerCommandPreprocessEvent 事件监听器
 * @author Jalexdalv
 */
@Listener
public class PlayerCommandPreprocessListener extends AbstractListener {
    @Autowired
    private Plugin plugin;
    @Autowired(file = "config.yml", path = "command.cd")
    private int cd;
    private final CdTimer cdTimer = new CdTimer();

    /**
     * PlayerCommandPreprocessEvent 事件发生时处理
     * @param playerCommandPreprocessEvent PlayerCommandPreprocessEvent 事件
     */
    @EventHandler
    public void onPlayerCommandPreprocess(@NotNull PlayerCommandPreprocessEvent playerCommandPreprocessEvent) {
        Player player = playerCommandPreprocessEvent.getPlayer();
        cdTimer.run(player, cd, false, null,
            () -> {
                if (!player.isOp()) {
                    String[] messages = StringUtils.split(playerCommandPreprocessEvent.getMessage(), " ");
                    if (!plugin.isCommand(StringUtils.replaceOnce(messages[0], "/", ""))) {
                        playerCommandPreprocessEvent.setCancelled(true);
                        player.sendMessage(Zh.UNKNOWN_COMMAND);
                    }
                    return true;
                }
                return false;
            },
            restTime -> {
                playerCommandPreprocessEvent.setCancelled(true);
                player.sendMessage(Zh.USE_COMMAND_FAST);
            }
        );
    }
}
