package org.huanshi.mc.lib.command;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.lang.Zh;
import org.huanshi.mc.lib.service.CombatService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 抽象命令
 * @author Jalexdalv
 */
public abstract class AbstractCommand implements TabExecutor {
    @Autowired
    private CombatService combatService;
    private Environment environment;
    private boolean op, combat;
    private String permission, head;
    private List<String> argList;
    private final List<String> emptyTabList = new ArrayList<>();

    /**
     * 加载
     * @param environment 环境
     * @param op 是否需要OP
     * @param combat 是否可以处于战斗状态使用
     * @param permission 权限
     * @param head 指令头
     * @param args 参数
     */
    public void load(Environment environment, boolean op, boolean combat, @Nullable String permission, @NotNull String head, @NotNull String @NotNull [] args) {
        this.environment = environment;
        this.op = op;
        this.combat = combat;
        this.permission = permission;
        this.head = head;
        argList = Arrays.asList(args);
    }

    /**
     * 执行指令
     * @param commandSender 指令发送者
     * @param command 指令
     * @param head 指令头
     * @param args 参数
     * @return 是否执行指令
     */
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String head, @NotNull String @NotNull [] args) {
        if (commandSender instanceof Player player) {
            if (environment == Environment.CONSOLE) {
                player.sendMessage(Zh.ONLY_CONSOLE);
            } else {
                return !canUse(player) || onPlayerCommand(player, args);
            }
        } else if (commandSender instanceof ConsoleCommandSender consoleCommandSender) {
            if (environment == Environment.GAME) {
                commandSender.sendMessage(Zh.ONLY_GAME);
            } else {
                return onConsoleCommand(consoleCommandSender, args);
            }
        }
        return true;
    }

    /**
     * 执行指令补全
     * @param commandSender 指令发送者
     * @param command 指令
     * @param head 指令头
     * @param args 参数
     * @return 是否执行指令补全
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String head, @NotNull String @NotNull [] args) {
        if (!(commandSender instanceof Player player)) {
            return null;
        }
        return onPlayerTabComplete(player, args);
    }

    /**
     * 执行控制台指令
     * @param consoleCommandSender 控制台
     * @param args 参数
     * @return 是否执行控制台指令
     */
    protected boolean onConsoleCommand(@NotNull ConsoleCommandSender consoleCommandSender, @NotNull String @NotNull [] args) {
        return true;
    }

    /**
     * 执行玩家指令
     * @param player 玩家
     * @param args 参数
     * @return 是否执行玩家指令
     */
    protected boolean onPlayerCommand(@NotNull Player player, @NotNull String @NotNull [] args) {
        return true;
    }

    /**
     * 执行玩家指令补全
     * @param player 玩家
     * @param args 参数
     * @return 是否执行玩家指令补全
     */
    protected @Nullable List<String> onPlayerTabComplete(@NotNull Player player, @NotNull String @NotNull [] args) {
        return emptyTabList;
    }

    /**
     * 查找玩家
     * @param player 玩家
     * @param targetPlayerName 目标玩家名
     * @return 目标玩家
     */
    public @Nullable Player findPlayer(@NotNull Player player, @NotNull String targetPlayerName) {
        Player targetPlayer = Bukkit.getPlayerExact(targetPlayerName);
        if (targetPlayer == null) {
            player.sendMessage(Zh.PLAYER_NOT_FOUND);
        }
        return targetPlayer;
    }

    /**
     * 查找世界
     * @param player 玩家
     * @param worldName 世界名
     * @return 世界
     */
    public @Nullable World findWorld(@NotNull Player player, @NotNull String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            player.sendMessage(Zh.WORLD_NOT_FOUND);
        }
        return world;
    }

    /**
     * 注册
     */
    public void register() {
        PluginCommand pluginCommand = Objects.requireNonNull(Bukkit.getPluginCommand(head));
        pluginCommand.setExecutor(this);
        pluginCommand.setTabCompleter(this);
    }

    /**
     * 判断是否可以使用
     * @param player 玩家
     * @return 是否可以使用
     */
    public boolean canUse(@NotNull Player player) {
        if (!combat && combatService.isCombating(player.getUniqueId())) {
            player.sendMessage(Zh.CANNOT_USE_COMMAND_IN_COMBAT);
            return false;
        } else if (!hasPermission(player)) {
            player.sendMessage(Zh.NO_PERMISSION);
            return false;
        }
        return true;
    }

    /**
     * 判断是否拥有权限
     * @param player 玩家
     * @return 是否拥有权限
     */
    public boolean hasPermission(@NotNull Player player) {
        return player.isOp() || (!isOp() && (permission == null || player.hasPermission(permission)));
    }

    /**
     * 获取环境
     * @return 环境
     */
    public @NotNull Environment getEnvironment() {
        return environment;
    }

    /**
     * 判断是否需要OP
     * @return 是否需要OP
     */
    public boolean isOp() {
        return op;
    }

    /**
     * 判断是否可以处于战斗状态使用
     * @return 是否可以处于战斗状态使用
     */
    public boolean isCombating() {
        return combat;
    }

    /**
     * 获取权限
     * @return 权限
     */
    public @Nullable String getPermission() {
        return permission;
    }

    /**
     * 获取指令头
     * @return 指令头
     */
    public @NotNull String getHead() {
        return head;
    }

    /**
     * 获取参数
     * @return 参数
     */
    public @NotNull List<String> getArgs() {
        return argList;
    }
}
