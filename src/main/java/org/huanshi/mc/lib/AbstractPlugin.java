package org.huanshi.mc.lib;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.huanshi.mc.lib.core.Loader;
import org.huanshi.mc.lib.lang.Zh;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Set;

/**
 * 抽象插件
 * @author Jalexdalv
 */
public abstract class AbstractPlugin extends JavaPlugin {
    /**
     * 启动
     */
    @Override
    public void onEnable() {
        createFolder();
        load();
        enable();
        sendConsoleMessage(Zh.enable(getName()));
    }

    /**
     * 关闭
     */
    @Override
    public void onDisable() {
        clearAllTask();
        disable();
        sendConsoleMessage(Zh.disable(getName()));
    }

    /**
     * 启动处理
     */
    protected void enable() {}

    /**
     * 关闭处理
     */
    protected void disable() {}

    /**
     * 加载
     */
    protected void load() {
        try {
            Loader.scan(this);
            Loader.load(this);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    /**
     * 重载
     */
    public void reLoad() {
        try {
            Loader.load(this);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    /**
     * 创建插件文件夹
     */
    protected void createFolder() {
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            if (!dataFolder.mkdirs()) {
                throw new RuntimeException("插件文件夹创建失败");
            }
        }
    }

    /**
     * 清除所有任务
     */
    protected void clearAllTask() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    /**
     * 发送控制台消息
     * @param component 消息
     */
    protected void sendConsoleMessage(@NotNull Component component) {
        getServer().getConsoleSender().sendMessage(component);
    }

    /**
     * 判断是否为指令
     * @param command 指令
     * @return 是否为指令
     */
    public boolean isCommand(@NotNull String command) {
        return Loader.isCommand(command);
    }

    /**
     * 判断是否为OP指令
     * @param command 指令
     * @return 是否为OP指令
     */
    public boolean isOpCommand(@NotNull String command) {
        return Loader.isOpCommand(command);
    }

    /**
     * 获取指令集合
     * @return 指令集合
     */
    public @NotNull Set<String> getCommands() {
        return Loader.getCommands();
    }

    /**
     * 获取OP指令集合
     * @return OP指令集合
     */
    public @NotNull Set<String> getOpCommands() {
        return Loader.getOpCommands();
    }
}
