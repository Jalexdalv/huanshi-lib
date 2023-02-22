package org.huanshi.mc.lib.lang;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.huanshi.mc.framework.utils.FormatUtils;
import org.jetbrains.annotations.NotNull;

public class Zh {
    public static final Component UNKNOWN_COMMAND = Component.text("未知指令", NamedTextColor.RED);
    public static final Component USE_COMMAND_FAST = Component.text("指令发送的太快了", NamedTextColor.RED);

    private static final Component COMBAT = Component.text(" 秒后离开战斗状态", NamedTextColor.RED);
    public static @NotNull Component combat(long duration) {
        return Component.text(FormatUtils.convertMillisecondToSecond(duration), NamedTextColor.YELLOW).append(COMBAT);
    }
}
