package org.huanshi.mc.lib.service;

import org.huanshi.mc.framework.service.AbstractService;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class CommandService extends AbstractService {
    private final Set<String> commandHeadSet = new HashSet<>(), opCommandHeadSet = new HashSet<>();

    public final void addCommandHead(@NotNull final String head) {
        commandHeadSet.add(head);
    }

    public final void addOpCommandHead(@NotNull final String head) {
        opCommandHeadSet.add(head);
    }

    public final boolean isCommandHead(@NotNull final String head) {
        return commandHeadSet.contains(head) || opCommandHeadSet.contains(head);
    }

    public final @NotNull Set<String> getCommandHeads() {
        return commandHeadSet;
    }

    public final @NotNull Set<String> getOpCommandHeads() {
        return opCommandHeadSet;
    }
}
