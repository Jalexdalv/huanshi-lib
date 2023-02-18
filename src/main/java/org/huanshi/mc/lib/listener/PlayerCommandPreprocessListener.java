package org.huanshi.mc.lib.listener;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Listener;
import org.huanshi.mc.lib.config.MainConfig;
import org.huanshi.mc.lib.lang.Zh;
import org.huanshi.mc.lib.manager.CommandManager;
import org.huanshi.mc.lib.timer.Cooldowner;
import org.jetbrains.annotations.NotNull;

@Listener
public class PlayerCommandPreprocessListener extends AbstractListener {
    @Autowired
    private MainConfig mainConfig;
    @Autowired
    private CommandManager commandManager;
    private long cd;
    private final Cooldowner cooldowner = new Cooldowner();

    @Override
    public void onLoad() {
        cd = mainConfig.getLong("command.cd");
    }

    @EventHandler
    public void onPlayerCommandPreprocess(@NotNull PlayerCommandPreprocessEvent playerCommandPreprocessEvent) {
        Player player = playerCommandPreprocessEvent.getPlayer();
        cooldowner.start(player, cd,
            () -> {
                String command = StringUtils.replaceOnce(StringUtils.split(playerCommandPreprocessEvent.getMessage(), " ")[0], "/", "");
                if (!commandManager.isCommand(command) && !commandManager.isOpCommand(command)) {
                    playerCommandPreprocessEvent.setCancelled(true);
                    player.sendMessage(Zh.UNKNOWN_COMMAND);
                }
                return true;
            }, durationLeft -> {
                playerCommandPreprocessEvent.setCancelled(true);
                player.sendMessage(Zh.USE_COMMAND_FAST);
                return false;
            }
        );
    }
}
