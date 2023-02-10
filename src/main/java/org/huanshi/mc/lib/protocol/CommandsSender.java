package org.huanshi.mc.lib.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Player;
import org.huanshi.mc.lib.annotation.Sender;
import org.jetbrains.annotations.NotNull;

/**
 * Commands 数据包发送器
 * @author Jalexdalv
 */
@Sender
public class CommandsSender extends AbstractSender {
    /**
     * 自动重生
     * @param player 玩家
     */
    public void autoRespawn(@NotNull Player player) {
        PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.COMMANDS);
        packetContainer.getClientCommands().write(0, EnumWrappers.ClientCommand.PERFORM_RESPAWN);
        getProtocolManager().sendServerPacket(player, packetContainer, false);
    }
}
