package org.huanshi.mc.lib.protocol;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.jetbrains.annotations.NotNull;

/**
 * 抽象数据传输协议
 * @author Jalexdalv
 */
public abstract class AbstractProtocol {
    private static ProtocolManager protocolManager;

    /**
     * 加载
     */
    public void load() {
        if (protocolManager == null) {
            protocolManager = ProtocolLibrary.getProtocolManager();
        }
    }

    /**
     * 获取数据传输协议管理器
     * @return 数据传输协议管理器
     */
    protected @NotNull ProtocolManager getProtocolManager() {
        return protocolManager;
    }
}
