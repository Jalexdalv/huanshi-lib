package org.huanshi.mc.lib.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.huanshi.mc.lib.Plugin;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.SendHandler;
import org.huanshi.mc.lib.annotation.Sender;
import org.huanshi.mc.lib.event.PlayerBlockEvent;

@Sender
public class BlockChangedAckSender extends AbstractSender {
    @Autowired
    private Plugin plugin;

    @SendHandler
    public PacketAdapter blockChangedAck() {
        return new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.BLOCK_CHANGED_ACK) {
            @Override
            public void onPacketSending(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player.isHandRaised()) {
                    Bukkit.getPluginManager().callEvent(new PlayerBlockEvent(player));
                }
            }
        };
    }
}
