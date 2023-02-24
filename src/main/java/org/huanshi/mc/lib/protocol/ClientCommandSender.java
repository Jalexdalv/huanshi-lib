package org.huanshi.mc.lib.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Player;
import org.huanshi.mc.framework.protocol.AbstractProtocol;
import org.jetbrains.annotations.NotNull;

public class ClientCommandSender extends AbstractProtocol {
    public void autoRespawn(@NotNull Player player) {
        PacketContainer packetContainer = new PacketContainer(PacketType.Play.Client.CLIENT_COMMAND);
        packetContainer.getClientCommands().write(0, EnumWrappers.ClientCommand.PERFORM_RESPAWN);
        PROTOCOL_MANAGER.receiveClientPacket(player, packetContainer, false);
    }
}
