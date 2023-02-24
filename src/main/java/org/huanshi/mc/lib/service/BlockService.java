package org.huanshi.mc.lib.service;

import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.api.BukkitAPI;
import org.huanshi.mc.framework.service.HuanshiService;
import org.huanshi.mc.lib.Plugin;
import org.huanshi.mc.lib.event.PlayerBlockEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BlockService extends HuanshiService {
    @Autowired
    private Plugin plugin;
    protected final Map<UUID, Boolean> blockMap = new ConcurrentHashMap<>();

    public void check() {
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

    public void clear(@NotNull UUID uuid) {
        blockMap.remove(uuid);
    }

    public void clear(@NotNull Player player) {
        clear(player.getUniqueId());
    }
}
