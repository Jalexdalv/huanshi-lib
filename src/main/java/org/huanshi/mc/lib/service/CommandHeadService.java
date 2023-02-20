package org.huanshi.mc.lib.service;

import org.huanshi.mc.framework.service.AbstractService;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class CommandHeadService extends AbstractService {
    private final Set<String> commandHeadSet = new HashSet<>(), opCommandHeadSet = new HashSet<>();

    public void addCommandHead(@NotNull String head) {
        commandHeadSet.add(head);
    }

    public void addOpCommandHead(@NotNull String head) {
        opCommandHeadSet.add(head);
    }

    public boolean isCommandHead(@NotNull String head) {
        return commandHeadSet.contains(head) || opCommandHeadSet.contains(head);
    }

    public @NotNull Set<String> getCommandHeads() {
        return commandHeadSet;
    }

    public @NotNull Set<String> getOpCommandHeads() {
        return opCommandHeadSet;
    }
}
