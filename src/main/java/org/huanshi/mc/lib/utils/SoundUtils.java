package org.huanshi.mc.lib.utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

public class SoundUtils {
    public static void play(@NotNull final Location location, @NotNull final Sound sound, final float volume, final float pitch) {
        TargetUtils.aimPlayers(location, 20.0D, 10.0D, 20.0D, -20.0D, -10.0D, -20.0D, null, player -> player.playSound(location, sound, volume, pitch));
    }
}
