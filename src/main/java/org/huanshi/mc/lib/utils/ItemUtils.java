package org.huanshi.mc.lib.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * 物品工具类
 * @author Jalexdalv
 */
public class ItemUtils {
    private static final Set<Material> SWORD_SET = new HashSet<>() {{
        add(Material.WOODEN_SWORD);
        add(Material.STONE_SWORD);
        add(Material.IRON_SWORD);
        add(Material.GOLDEN_SWORD);
        add(Material.DIAMOND_SWORD);
        add(Material.NETHERITE_SWORD);
    }};
    private static final Set<Material> AXE_SET = new HashSet<>() {{
        add(Material.WOODEN_AXE);
        add(Material.STONE_AXE);
        add(Material.IRON_AXE);
        add(Material.GOLDEN_AXE);
        add(Material.DIAMOND_AXE);
        add(Material.NETHERITE_AXE);
    }};

    /**
     * 判断是否为剑
     * @param itemStack 物品堆
     * @return 是否为剑
     */
    public static boolean isSword(@NotNull ItemStack itemStack) {
        return SWORD_SET.contains(itemStack.getType());
    }

    /**
     * 判断是否为斧子
     * @param itemStack 物品堆
     * @return 是否为斧子
     */
    public static boolean isAxe(@NotNull ItemStack itemStack) {
        return AXE_SET.contains(itemStack.getType());
    }

    /**
     * 判断副手是否为盾牌
     * @param player 玩家
     * @return 副手是否为盾牌
     */
    public static boolean isShieldInOffHand(@NotNull Player player) {
        PlayerInventory playerInventory = player.getInventory();
        return playerInventory.getItemInMainHand().getType() != Material.SHIELD && playerInventory.getItemInOffHand().getType() == Material.SHIELD;
    }

    /**
     * 判断是否主手单手持剑
     * @param player 玩家
     * @return 是否主手单手持剑
     */
    public static boolean isSingleSwordInMainHand(@NotNull Player player) {
        PlayerInventory playerInventory = player.getInventory();
        return isSword(playerInventory.getItemInMainHand()) && !isSword(playerInventory.getItemInOffHand());
    }

    /**
     * 判断是否副手单手持剑
     * @param player 玩家
     * @return 是否副手单手持剑
     */
    public static boolean isSingleSwordInOffHand(@NotNull Player player) {
        PlayerInventory playerInventory = player.getInventory();
        return !isSword(playerInventory.getItemInMainHand()) && isSword(playerInventory.getItemInOffHand());
    }

    /**
     * 判断是否双手持剑
     * @param player 玩家
     * @return 是否双手持剑
     */
    public static boolean isDoubleSwordInHand(@NotNull Player player) {
        PlayerInventory playerInventory = player.getInventory();
        return isSword(playerInventory.getItemInMainHand()) && isSword(playerInventory.getItemInOffHand());
    }

    /**
     * 判断是否主手单手持斧
     * @param player 玩家
     * @return 是否主手单手持斧
     */
    public static boolean isSingleAxeInMainHand(@NotNull Player player) {
        PlayerInventory playerInventory = player.getInventory();
        return isAxe(playerInventory.getItemInMainHand()) && !isAxe(playerInventory.getItemInOffHand());
    }

    /**
     * 判断是否双手持斧
     * @param player 玩家
     * @return 是否双手持斧
     */
    public static boolean isDoubleAxeInHand(@NotNull Player player) {
        PlayerInventory playerInventory = player.getInventory();
        return isAxe(playerInventory.getItemInMainHand()) && isAxe(playerInventory.getItemInOffHand());
    }
}
