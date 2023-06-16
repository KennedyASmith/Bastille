package com.kennedysmithjava.prisoncore.util;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Color {

    public static String get(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> get(List<String> strings){
        List<String> newList = new ArrayList<>();
        strings.forEach(s -> newList.add(Color.get(s)));
        return newList;
    }

    public static String getGradient(String message, String color1, String color2) {
        int length = message.length();
        int colorCount = length - 1;

        int r1 = Integer.parseInt(color1.substring(0, 2), 16);
        int g1 = Integer.parseInt(color1.substring(2, 4), 16);
        int b1 = Integer.parseInt(color1.substring(4, 6), 16);

        int r2 = Integer.parseInt(color2.substring(0, 2), 16);
        int g2 = Integer.parseInt(color2.substring(2, 4), 16);
        int b2 = Integer.parseInt(color2.substring(4, 6), 16);

        StringBuilder coloredMessage = new StringBuilder();

        for (int i = 0; i < length; i++) {
            char c = message.charAt(i);

            if (i < colorCount) {
                double progress = (double) i / colorCount;

                int r = (int) (r1 + (r2 - r1) * progress);
                int g = (int) (g1 + (g2 - g1) * progress);
                int b = (int) (b1 + (b2 - b1) * progress);

                String colorCode = String.format("#%02X%02X%02X", r, g, b);
                coloredMessage.append(net.md_5.bungee.api.ChatColor.of(colorCode));
            }

            coloredMessage.append(c);
        }

        return coloredMessage.toString();
    }

    public static String strip(String msg){
        return ChatColor.stripColor(Color.get(msg));
    }
}
