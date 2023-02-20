package org.huanshi.mc.lib.utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

public class SoundUtils {
    public static void playSound(@NotNull Location location, @NotNull Sound sound, float volume, float pitch) {
        TargetUtils.aimPlayers(location, 20.0D, 10.0D, 20.0D, -20.0D, -10.0D, -20.0D, null, player -> player.playSound(location, sound, volume, pitch));
    }
}
