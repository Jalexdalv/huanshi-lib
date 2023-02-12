package org.huanshi.mc.lib.lang;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.huanshi.mc.lib.utils.TimerUtils;
import org.jetbrains.annotations.NotNull;

/**
 * 简体中文语言包
 * @author Jalexdalv
 */
public class Zh {
    public static final Component ONLY_CONSOLE = Component.text("该指令只能在后台执行", NamedTextColor.RED);
    public static final Component ONLY_GAME = Component.text("该指令只能在游戏内执行", NamedTextColor.RED);
    public static final Component NO_PERMISSION = Component.text("你没有权限", NamedTextColor.RED);
    public static final Component CANNOT_USE_COMMAND_IN_COMBAT = Component.text("战斗中无法使用该指令", NamedTextColor.RED);
    public static final Component PLAYER_NOT_FOUND = Component.text("该玩家不在线或不存在", NamedTextColor.RED);
    public static final Component WORLD_NOT_FOUND = Component.text("该世界不存在", NamedTextColor.RED);
    public static final Component UNKNOWN_COMMAND = Component.text("未知指令", NamedTextColor.RED);
    public static final Component USE_COMMAND_FAST = Component.text("指令发送的太快了", NamedTextColor.RED);

    public static final Component PLUGIN_NAME_1 = Component.text("[");
    public static final Component PLUGIN_NAME_2 = Component.text("] ");
    public static final Component ENABLE = Component.text("插件已加载", NamedTextColor.GREEN);
    /**
     * 获取启动文本
     * @param name 插件名
     * @return 启动文本
     */
    public static @NotNull Component enable(@NotNull String name) {
        return PLUGIN_NAME_1.append(Component.text(name)).append(PLUGIN_NAME_2).append(ENABLE);
    }
    public static final Component DISABLE = Component.text("插件已卸载", NamedTextColor.GREEN);
    /**
     * 获取关闭文本
     * @param name 插件名
     * @return 关闭文本
     */
    public static @NotNull Component disable(@NotNull String name) {
        return PLUGIN_NAME_1.append(Component.text(name)).append(PLUGIN_NAME_2).append(DISABLE);
    }
    private static final Component COMBAT = Component.text(" 秒后离开战斗状态", NamedTextColor.RED);
    /**
     * 获取战斗状态文本
     * @param duration 时长（毫秒）
     * @return 战斗状态文本
     */
    public static @NotNull Component combat(long duration) {
        return Component.text(TimerUtils.convertMillisecondToSecond(duration), NamedTextColor.YELLOW).append(COMBAT);
    }
    private static final Component CD_1 = Component.text("冷却中, 请等待 ", NamedTextColor.RED);
    private static final Component CD_2 = Component.text(" 秒", NamedTextColor.RED);
    /**
     * 获取CD文本
     * @param duration 时长（毫秒）
     * @return CD文本
     */
    public static @NotNull Component cd(long duration) {
        return CD_1.append(Component.text(TimerUtils.convertMillisecondToSecond(duration), NamedTextColor.YELLOW)).append(CD_2);
    }
    private static final Component LINK = Component.text(" - ", NamedTextColor.GRAY);
    private static final Component STUN_1 = Component.text("眩晕", NamedTextColor.RED);
    private static final Component STUN_2 = Component.text("秒", NamedTextColor.RED);
    /**
     * 获取眩晕状态文本
     * @param duration 时长（毫秒）
     * @return 眩晕状态文本
     */
    public static @NotNull Component stun(long duration) {
        return STUN_1.append(LINK).append(Component.text(TimerUtils.convertMillisecondToSecond(duration), NamedTextColor.YELLOW)).append(STUN_2);
    }
    private static final Component ROOT_1 = Component.text("禁锢", NamedTextColor.DARK_PURPLE);
    private static final Component ROOT_2 = Component.text("秒", NamedTextColor.DARK_PURPLE);
    /**
     * 获取禁锢状态文本
     * @param duration 时长（毫秒）
     * @return 禁锢状态文本
     */
    public static @NotNull Component root(long duration) {
        return ROOT_1.append(LINK).append(Component.text(TimerUtils.convertMillisecondToSecond(duration), NamedTextColor.YELLOW)).append(ROOT_2);
    }
    private static final Component SILENCE_1 = Component.text("沉默", NamedTextColor.LIGHT_PURPLE);
    private static final Component SILENCE_2 = Component.text("秒", NamedTextColor.LIGHT_PURPLE);
    /**
     * 获取沉默状态文本
     * @param duration 时长（毫秒）
     * @return 沉默状态文本
     */
    public static @NotNull Component silence(long duration) {
        return SILENCE_1.append(LINK).append(Component.text(TimerUtils.convertMillisecondToSecond(duration), NamedTextColor.YELLOW)).append(SILENCE_2);
    }
    private static final Component STEADY_1 = Component.text("霸体", NamedTextColor.GREEN);
    private static final Component STEADY_2 = Component.text("秒", NamedTextColor.GREEN);
    /**
     * 获取霸体状态文本
     * @param duration 时长（毫秒）
     * @return 霸体状态文本
     */
    public static @NotNull Component steady(long duration) {
        return STEADY_1.append(LINK).append(Component.text(TimerUtils.convertMillisecondToSecond(duration), NamedTextColor.YELLOW)).append(STEADY_2);
    }
    private static final Component INVINCIBLE_1 = Component.text("霸体", NamedTextColor.GREEN);
    private static final Component INVINCIBLE_2 = Component.text("秒", NamedTextColor.GREEN);
    /**
     * 获取无敌状态文本
     * @param duration 时长（毫秒）
     * @return 无敌状态文本
     */
    public static @NotNull Component invincible(long duration) {
        return INVINCIBLE_1.append(LINK).append(Component.text(TimerUtils.convertMillisecondToSecond(duration), NamedTextColor.YELLOW)).append(INVINCIBLE_2);
    }
}
