package org.huanshi.mc.lib.timer;

public interface CooldownerRunHandler {
    boolean handle(final long durationLeft);
}
