package org.hunseong.gachaSystem.util;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//"&#458a43테스트" - 이런 형식

public class HexColor {
    private final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private final char COLOR_CHAR = ChatColor.COLOR_CHAR;

    public String hexFormat(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        String hexMessage = matcher.appendTail(buffer).toString();
        return ChatColor.translateAlternateColorCodes('&', hexMessage);
    }
}
