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

}
