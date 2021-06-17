package com.kennedysmithjava.prisonmines.util;

import org.bukkit.ChatColor;

public class Color {


    public static String get(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }


}
