package org.huanshi.mc.lib.service;

import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.huanshi.mc.framework.service.AbstractService;
import org.huanshi.mc.lib.Plugin;
import org.huanshi.mc.lib.event.PlayerBlockEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerBlockService extends AbstractService {
    @Autowired
    private Plugin plugin;
    private final Map<UUID, Boolean> blockMap = new HashMap<>();

    public synchronized void check() {
        for (Player player : ImmutableList.copyOf(BukkitAPI.getOnlinePlayers())) {
            blockMap.compute(player.getUniqueId(), (uuid, blocking) -> {
                if (!player.isBlocking()) {
                    return false;
                }
                if (blocking == null || !blocking) {
                    BukkitAPI.runTask(plugin, () -> BukkitAPI.callEvent(new PlayerBlockEvent(player)));
                }
                return true;
            });
        }
    }

    public synchronized void clear(@NotNull UUID uuid) {
        blockMap.remove(uuid);
    }

    public synchronized void clear(@NotNull Player player) {
        clear(player.getUniqueId());
    }
}
