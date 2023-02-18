package org.huanshi.mc.lib.manager;

import org.huanshi.mc.lib.engine.Component;

public abstract class AbstractManager implements Component {
    @Override
    public void load() {}

    @Override
    public void onLoad() {}
}
