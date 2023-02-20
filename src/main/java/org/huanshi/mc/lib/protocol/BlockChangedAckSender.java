package org.huanshi.mc.lib.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.annotation.ProtocolHandler;
import org.huanshi.mc.framework.protocol.AbstractProtocol;
import org.huanshi.mc.lib.Plugin;
import org.huanshi.mc.lib.event.PlayerBlockEvent;
import org.jetbrains.annotations.NotNull;

public class BlockChangedAckSender extends AbstractProtocol {
    @Autowired
    private Plugin plugin;

    @ProtocolHandler
    public final @NotNull PacketAdapter onBlockChangedAck() {
        return new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.BLOCK_CHANGED_ACK) {
            @Override
            public void onPacketSending(final PacketEvent packetEvent) {
                final Player player = packetEvent.getPlayer();
                if (player.isHandRaised()) {
                    Bukkit.getPluginManager().callEvent(new PlayerBlockEvent(player));
                }
            }
        };
    }
}
