package org.huanshi.mc.lib.task;

import org.bukkit.entity.Player;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.annotation.HuanshiTask;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.huanshi.mc.framework.task.AbstractTask;
import org.huanshi.mc.lib.Plugin;
import org.huanshi.mc.lib.event.PlayerBlockEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@HuanshiTask(async = true, period = 100L)
public class PlayerBlockTask extends AbstractTask {
    @Autowired
    private Plugin plugin;
    private final Map<UUID, Boolean> blockMap = new HashMap<>();

    @Override
    public void run() {
        for (Player player : List.copyOf(BukkitAPI.getOnlinePlayers())) {
            Boolean blocking = blockMap.get(player.getUniqueId());
            if (player.isBlocking()) {
                if (blocking == null || !blocking) {
                    BukkitAPI.runTask(plugin, () -> BukkitAPI.callEvent(new PlayerBlockEvent(player)));
                }
                blockMap.put(player.getUniqueId(), true);
            } else {
                blockMap.put(player.getUniqueId(), false);
            }
        }
    }
}
