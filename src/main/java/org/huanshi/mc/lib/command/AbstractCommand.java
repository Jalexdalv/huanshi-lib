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

public abstract class AbstractCommand implements TabExecutor {
    @Autowired
    private CombatService combatService;
    private Environment environment;
    private boolean op, combat;
    private String permission, head;
    private List<String> argList;
    private final List<String> emptyTabList = new ArrayList<>();

    public void load(Environment environment, boolean op, boolean combat, @Nullable String permission, @NotNull String head, @NotNull String @NotNull [] args) {
        this.environment = environment;
        this.op = op;
        this.combat = combat;
        this.permission = permission;
        this.head = head;
        argList = Arrays.asList(args);
    }

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

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String head, @NotNull String @NotNull [] args) {
        if (commandSender instanceof Player player) {
            return onPlayerTabComplete(player, args);
        }
        return null;
    }

    protected boolean onConsoleCommand(@NotNull ConsoleCommandSender consoleCommandSender, @NotNull String @NotNull [] args) {
        return true;
    }

    protected boolean onPlayerCommand(@NotNull Player player, @NotNull String @NotNull [] args) {
        return true;
    }

    protected @Nullable List<String> onPlayerTabComplete(@NotNull Player player, @NotNull String @NotNull [] args) {
        return emptyTabList;
    }

    public @Nullable Player findPlayer(@NotNull Player player, @NotNull String targetPlayerName) {
        Player targetPlayer = Bukkit.getPlayerExact(targetPlayerName);
        if (targetPlayer == null) {
            player.sendMessage(Zh.PLAYER_NOT_FOUND);
        }
        return targetPlayer;
    }

    public @Nullable World findWorld(@NotNull Player player, @NotNull String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            player.sendMessage(Zh.WORLD_NOT_FOUND);
        }
        return world;
    }

    public void register() {
        PluginCommand pluginCommand = Objects.requireNonNull(Bukkit.getPluginCommand(head));
        pluginCommand.setExecutor(this);
        pluginCommand.setTabCompleter(this);
    }

    public boolean canUse(@NotNull Player player) {
        if (!combat && combatService.isRunning(player.getUniqueId())) {
            player.sendMessage(Zh.CANNOT_USE_COMMAND_IN_COMBAT);
            return false;
        } else if (!hasPermission(player)) {
            player.sendMessage(Zh.NO_PERMISSION);
            return false;
        }
        return true;
    }

    public boolean hasPermission(@NotNull Player player) {
        return player.isOp() || (!isOp() && (permission == null || player.hasPermission(permission)));
    }

    public @NotNull Environment getEnvironment() {
        return environment;
    }

    public boolean isOp() {
        return op;
    }

    public boolean isCombating() {
        return combat;
    }

    public @Nullable String getPermission() {
        return permission;
    }

    public @NotNull String getHead() {
        return head;
    }

    public @NotNull List<String> getArgs() {
        return argList;
    }
}
