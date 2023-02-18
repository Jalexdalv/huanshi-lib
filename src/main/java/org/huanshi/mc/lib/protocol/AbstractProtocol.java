package org.huanshi.mc.lib.protocol;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import org.huanshi.mc.lib.annotation.ProtocolHandler;
import org.huanshi.mc.lib.engine.Component;
import org.huanshi.mc.lib.engine.Registrable;
import org.huanshi.mc.lib.utils.ReflectUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class AbstractProtocol implements Component, Registrable {
    private static ProtocolManager protocolManager;

    @Override
    public void load() throws InvocationTargetException, IllegalAccessException {
        if (protocolManager == null) {
            protocolManager = ProtocolLibrary.getProtocolManager();
        }
    }

    @Override
    public void onLoad() {}

    @Override
    public void register() throws InvocationTargetException, IllegalAccessException {
        for (Method method : ReflectUtils.getMethods(getClass())) {
            if (method.getAnnotation(ProtocolHandler.class) != null && method.getReturnType() == PacketAdapter.class) {
                getProtocolManager().addPacketListener((PacketAdapter) method.invoke(this));
            }
        }
    }

    protected @NotNull ProtocolManager getProtocolManager() {
        return protocolManager;
    }
}
