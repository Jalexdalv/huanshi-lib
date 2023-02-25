package org.huanshi.mc.lib.task;

import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.annotation.HuanshiTask;
import org.huanshi.mc.framework.task.AbstractTask;
import org.huanshi.mc.lib.service.PlayerBlockService;

@HuanshiTask(async = true, period = 100L)
public class PlayerBlockTask extends AbstractTask {
    @Autowired
    private PlayerBlockService playerBlockService;

    @Override
    public void run() {
        playerBlockService.check();
    }
}
