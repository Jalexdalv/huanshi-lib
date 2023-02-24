package org.huanshi.mc.lib.task;

import org.huanshi.mc.framework.annotation.Autowired;
import org.huanshi.mc.framework.annotation.Task;
import org.huanshi.mc.framework.task.HuanshiTask;
import org.huanshi.mc.lib.service.BlockService;

@Task(async = true, period = 100L)
public class PlayerBlockTask extends HuanshiTask {
    @Autowired
    private BlockService blockService;

    @Override
    public void run() {
        blockService.check();
    }
}
