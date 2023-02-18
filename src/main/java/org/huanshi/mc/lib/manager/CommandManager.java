package org.huanshi.mc.lib.manager;

import org.huanshi.mc.lib.annotation.Manager;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@Manager
public class CommandManager extends AbstractManager {
    private final Set<String> commandSet = new HashSet<>();
    private final Set<String> opCommandSet = new HashSet<>();

    public void addCommand(@NotNull String head) {
        commandSet.add(head);
    }

    public void addOpCommand(@NotNull String head) {
        opCommandSet.add(head);
    }

    public Set<String> getCommands() {
        return commandSet;
    }

    public Set<String> getOpCommands() {
        return opCommandSet;
    }

    public boolean isCommand(@NotNull String head) {
        return commandSet.contains(head);
    }

    public boolean isOpCommand(@NotNull String head) {
        return opCommandSet.contains(head);
    }
}
