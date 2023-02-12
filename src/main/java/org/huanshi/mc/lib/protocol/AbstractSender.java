package org.huanshi.mc.lib.protocol;

import com.comphenix.protocol.events.PacketAdapter;
import org.huanshi.mc.lib.annotation.SendHandler;
import org.huanshi.mc.lib.utils.ReflectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class AbstractSender extends AbstractProtocol {
    public void register() throws InvocationTargetException, IllegalAccessException {
        for (Method method : ReflectUtils.getMethods(getClass())) {
            if (method.getAnnotation(SendHandler.class) != null && method.getReturnType() == PacketAdapter.class) {
                getProtocolManager().addPacketListener((PacketAdapter) method.invoke(this));
            }
        }
    }
}
