package org.huanshi.mc.lib.protocol;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractProtocol {
    private static ProtocolManager protocolManager;

    public void load() {
        if (protocolManager == null) {
            protocolManager = ProtocolLibrary.getProtocolManager();
        }
    }

    protected @NotNull ProtocolManager getProtocolManager() {
        return protocolManager;
    }
}
