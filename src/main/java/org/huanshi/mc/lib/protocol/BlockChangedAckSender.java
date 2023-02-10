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

/**
 * BlockChangedAck 数据包发送器
 * @author Jalexdalv
 */
@Sender
public class BlockChangedAckSender extends AbstractSender {
    @Autowired
    private Plugin plugin;

    /**
     * BlockChangedAck 数据包发送监听器
     * @return 数据包适配器
     */
    @SendHandler
    public PacketAdapter onBlockChangedAck() {
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
