package com.kennedysmithjava.prisoncore.eco;

import com.kennedysmithjava.prisoncore.util.Color;

public enum CurrencyType {
    CASH("Cash", "&aCash", "&a", "⛃"),
    RESEARCH("RP", "&bResearch Points","&b", "✪"),
    GEMS("Gems", "&dGems","&d", "۞")
    ;

    private final String displayName;
    private final String symbol;
    private final String color;

    CurrencyType(String name, String displayName, String color, String symbol) {
        this.displayName = displayName;
        this.symbol = symbol;
        this.color = color;
    }

    public String getDisplayName() {
        return Color.get(this.displayName);
    }

    public String getColor() {
        return color;
    }

    public String getSymbol(){
        return symbol;
    }

}
