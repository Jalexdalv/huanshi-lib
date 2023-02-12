package org.huanshi.mc.lib.listener;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Data;
import org.huanshi.mc.lib.annotation.Listener;
import org.huanshi.mc.lib.lang.Zh;
import org.huanshi.mc.lib.timer.CdTimer;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Listener
public class PlayerCommandPreprocessListener extends AbstractListener {
    @Autowired(file = "config.yml", path = "command.cd")
    private long cd;
    @Data(name = "COMMAND_SET")
    private Set<String> commandSet;
    @Data(name = "OP_COMMAND_SET")
    private Set<String> opCommandSet;
    private final CdTimer cdTimer = new CdTimer();

    @EventHandler
    public void onPlayerCommandPreprocess(@NotNull PlayerCommandPreprocessEvent playerCommandPreprocessEvent) {
        Player player = playerCommandPreprocessEvent.getPlayer();
        cdTimer.run(player.getUniqueId(), cd, false, null,
            () -> {
                String command = StringUtils.replaceOnce(StringUtils.split(playerCommandPreprocessEvent.getMessage(), " ")[0], "/", "");
                if (commandSet.contains(command) || opCommandSet.contains(command)) {
                    return true;
                }
                playerCommandPreprocessEvent.setCancelled(true);
                player.sendMessage(Zh.UNKNOWN_COMMAND);
                return false;
            }, remainDuration -> {
                playerCommandPreprocessEvent.setCancelled(true);
                player.sendMessage(Zh.USE_COMMAND_FAST);
            }
        );
    }
}
