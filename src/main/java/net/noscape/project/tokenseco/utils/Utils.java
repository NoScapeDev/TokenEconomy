package net.noscape.project.tokenseco.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.*;

import java.util.*;
import java.util.regex.*;

public class Utils {

    public static String applyFormat(String message) {
        message = message.replace(">>", "").replace("<<", "");
        Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]){6}");
        Matcher matcher = hexPattern.matcher(message);
        while (matcher.find()) {
            ChatColor hexColor = ChatColor.valueOf(matcher.group().substring(1));
            String before = message.substring(0, matcher.start());
            String after = message.substring(matcher.end());
            message = before + hexColor + after;
            matcher = hexPattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> applyFormatList(List<String> message) {
        return Collections.singletonList(ChatColor.translateAlternateColorCodes('&', String.valueOf(message)));
    }

    public static void msgPlayer(Player player, String... str) {
        for (String msg : str) {
            player.sendMessage(applyFormat(msg));
        }
    }
}