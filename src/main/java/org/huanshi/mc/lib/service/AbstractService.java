package org.huanshi.mc.lib.service;

import org.huanshi.mc.lib.engine.Component;

public abstract class AbstractService implements Component {
    @Override
    public void load() {}

    @Override
    public void onLoad() {}
}
