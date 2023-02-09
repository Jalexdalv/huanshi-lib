package org.huanshi.mc.lib;

import org.huanshi.mc.lib.lang.Zh;

public class Plugin extends AbstractPlugin {
    @Override
    public void onEnable() {
        createFolder();
        load();
        sendConsoleMessage(Zh.ENABLE);
    }

    @Override
    public void onDisable() {
        clearAllTask();
        sendConsoleMessage(Zh.DISABLE);
    }
}
