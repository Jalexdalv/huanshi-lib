package org.huanshi.mc.lib.engine;

public interface Component {
    void load() throws Throwable;
    void onLoad() throws Throwable;
}
