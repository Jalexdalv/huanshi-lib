package org.huanshi.mc.lib.service;

import org.huanshi.mc.framework.service.AbstractService;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class CommandNameService extends AbstractService {
    private final Set<String> nameSet = new HashSet<>(), opNameSet = new HashSet<>();

    public void addName(@NotNull String name) {
        nameSet.add(name);
    }

    public void addOpName(@NotNull String name) {
        opNameSet.add(name);
    }

    public boolean isCommand(@NotNull String name) {
        return nameSet.contains(name) || opNameSet.contains(name);
    }

    public @NotNull Set<String> getNames() {
        return nameSet;
    }

    public @NotNull Set<String> getOpNames() {
        return opNameSet;
    }
}
