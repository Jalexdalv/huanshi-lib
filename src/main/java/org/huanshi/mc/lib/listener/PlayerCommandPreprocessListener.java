package org.huanshi.mc.lib.listener;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Data;
import org.huanshi.mc.lib.annotation.Listener;
import org.huanshi.mc.lib.lang.Zh;
import org.huanshi.mc.lib.timer.Cooldowner;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Listener
public class PlayerCommandPreprocessListener extends AbstractListener {
    @Data(name = "COMMAND_SET")
    private Set<String> commandSet;
    @Data(name = "OP_COMMAND_SET")
    private Set<String> opCommandSet;
    @Autowired(file = "config.yml", path = "command.cd")
    private long cd;
    private final Cooldowner cooldowner = new Cooldowner();

    @EventHandler
    public void onPlayerCommandPreprocess(@NotNull PlayerCommandPreprocessEvent playerCommandPreprocessEvent) {
        Player player = playerCommandPreprocessEvent.getPlayer();
        cooldowner.start(player, cd, () -> {
            String command = StringUtils.replaceOnce(StringUtils.split(playerCommandPreprocessEvent.getMessage(), " ")[0], "/", "");
            if (!commandSet.contains(command) && !opCommandSet.contains(command)) {
                playerCommandPreprocessEvent.setCancelled(true);
                player.sendMessage(Zh.UNKNOWN_COMMAND);
            }
            return true;
        }, durationLeft -> {
            playerCommandPreprocessEvent.setCancelled(true);
            player.sendMessage(Zh.USE_COMMAND_FAST);
            return false;
        });
    }
}
