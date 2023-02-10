package org.huanshi.mc.lib.protocol;

import com.comphenix.protocol.events.PacketAdapter;
import org.huanshi.mc.lib.annotation.ReceiveHandler;
import org.huanshi.mc.lib.utils.ReflectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 抽象数据包接收器
 * @author Jalexdalv
 */
public abstract class AbstractReceiver extends AbstractProtocol {
    /**
     * 注册
     * @throws InvocationTargetException 调用对象异常
     * @throws IllegalAccessException 非法访问异常
     */
    public void register() throws InvocationTargetException, IllegalAccessException {
        for (Method method : ReflectUtils.getMethods(getClass())) {
            if (method.getAnnotation(ReceiveHandler.class) != null && method.getReturnType() == PacketAdapter.class) {
                getProtocolManager().addPacketListener((PacketAdapter) method.invoke(this));
            }
        }
    }
}
