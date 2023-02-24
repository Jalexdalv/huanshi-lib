package org.huanshi.mc.lib.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class ItemUtils {
    private static final Set<Material> SWORDS = new HashSet<>() {{
        add(Material.WOODEN_SWORD);
        add(Material.STONE_SWORD);
        add(Material.IRON_SWORD);
        add(Material.GOLDEN_SWORD);
        add(Material.DIAMOND_SWORD);
        add(Material.NETHERITE_SWORD);
    }};
    private static final Set<Material> AXES = new HashSet<>() {{
        add(Material.WOODEN_AXE);
        add(Material.STONE_AXE);
        add(Material.IRON_AXE);
        add(Material.GOLDEN_AXE);
        add(Material.DIAMOND_AXE);
        add(Material.NETHERITE_AXE);
    }};

    public static boolean isSword(@NotNull ItemStack itemStack) {
        return SWORDS.contains(itemStack.getType());
    }

    public static boolean isAxe(@NotNull ItemStack itemStack) {
        return AXES.contains(itemStack.getType());
    }

    public static boolean isShieldInOffHand(@NotNull Player player) {
        PlayerInventory playerInventory = player.getInventory();
        return playerInventory.getItemInMainHand().getType() != Material.SHIELD && playerInventory.getItemInOffHand().getType() == Material.SHIELD;
    }

    public static boolean isSingleSwordInMainHand(@NotNull Player player) {
        PlayerInventory playerInventory = player.getInventory();
        return isSword(playerInventory.getItemInMainHand()) && !isSword(playerInventory.getItemInOffHand());
    }

    public static boolean isSingleSwordInOffHand(@NotNull Player player) {
        PlayerInventory playerInventory = player.getInventory();
        return !isSword(playerInventory.getItemInMainHand()) && isSword(playerInventory.getItemInOffHand());
    }

    public static boolean isDoubleSwordInHand(@NotNull Player player) {
        PlayerInventory playerInventory = player.getInventory();
        return isSword(playerInventory.getItemInMainHand()) && isSword(playerInventory.getItemInOffHand());
    }

    public static boolean isSingleAxeInMainHand(@NotNull Player player) {
        PlayerInventory playerInventory = player.getInventory();
        return isAxe(playerInventory.getItemInMainHand()) && !isAxe(playerInventory.getItemInOffHand());
    }

    public static boolean isDoubleAxeInHand(@NotNull Player player) {
        PlayerInventory playerInventory = player.getInventory();
        return isAxe(playerInventory.getItemInMainHand()) && isAxe(playerInventory.getItemInOffHand());
    }
}
