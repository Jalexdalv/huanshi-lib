package org.huanshi.mc.lib.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Player;
import org.huanshi.mc.lib.annotation.Protocol;
import org.jetbrains.annotations.NotNull;

@Protocol
public class CommandsSender extends AbstractProtocol {
    public void autoRespawn(@NotNull Player player) {
        PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.COMMANDS);
        packetContainer.getClientCommands().write(0, EnumWrappers.ClientCommand.PERFORM_RESPAWN);
        getProtocolManager().sendServerPacket(player, packetContainer, false);
    }
}
