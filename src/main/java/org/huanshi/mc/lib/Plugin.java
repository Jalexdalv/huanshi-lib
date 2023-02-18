package org.huanshi.mc.lib;

import org.huanshi.mc.lib.annotation.Command;
import org.huanshi.mc.lib.annotation.Config;
import org.huanshi.mc.lib.annotation.Listener;
import org.huanshi.mc.lib.annotation.Manager;
import org.huanshi.mc.lib.annotation.Mapper;
import org.huanshi.mc.lib.annotation.Protocol;
import org.huanshi.mc.lib.annotation.Service;
import org.huanshi.mc.lib.annotation.Task;
import org.huanshi.mc.lib.command.AbstractCommand;
import org.huanshi.mc.lib.config.AbstractConfig;
import org.huanshi.mc.lib.engine.Loader;
import org.huanshi.mc.lib.listener.AbstractListener;
import org.huanshi.mc.lib.manager.AbstractManager;
import org.huanshi.mc.lib.mapper.AbstractMapper;
import org.huanshi.mc.lib.protocol.AbstractProtocol;
import org.huanshi.mc.lib.service.AbstractService;
import org.huanshi.mc.lib.task.AbstractTask;

public class Plugin extends AbstractPlugin {
    @Override
    protected void start() {
        Loader.register(AbstractCommand.class, Command.class);
        Loader.register(AbstractConfig.class, Config.class);
        Loader.register(AbstractListener.class, Listener.class);
        Loader.register(AbstractManager.class, Manager.class);
        Loader.register(AbstractMapper.class, Mapper.class);
        Loader.register(AbstractProtocol.class, Protocol.class);
        Loader.register(AbstractService.class, Service.class);
        Loader.register(AbstractTask.class, Task.class);
    }
}
