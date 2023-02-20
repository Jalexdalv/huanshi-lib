package org.huanshi.mc.lib.listener;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.listener.AbstractListener;
import org.huanshi.mc.lib.config.MainConfig;
import org.huanshi.mc.lib.lang.Zh;
import org.huanshi.mc.lib.service.CommandService;
import org.huanshi.mc.lib.timer.Cooldowner;
import org.jetbrains.annotations.NotNull;

public class PlayerCommandPreprocessListener extends AbstractListener {
    @Autowired
    private MainConfig mainConfig;
    @Autowired
    private CommandService commandService;
    private long cd;
    private final Cooldowner cooldowner = new Cooldowner();

    @Override
    public final void load() {
        cd = mainConfig.getLong("command.cd");
    }

    @EventHandler
    public final void onPlayerCommandPreprocess(@NotNull final PlayerCommandPreprocessEvent playerCommandPreprocessEvent) {
        final Player player = playerCommandPreprocessEvent.getPlayer();
        cooldowner.start(player, cd,
            () -> {
                final String command = StringUtils.replaceOnce(StringUtils.split(playerCommandPreprocessEvent.getMessage(), " ")[0], "/", "");
                if (!commandService.isCommandHead(command)) {
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
