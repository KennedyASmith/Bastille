package com.kennedysmithjava.prisoncore.eco;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.util.Color;

import java.text.DecimalFormat;

public class CostCurrency extends Cost{

    private final CurrencyType currencyType;
    private final double amount;

    public CostCurrency(CurrencyType currencyType, double amount){
        this.currencyType = currencyType;
        this.amount = amount;
    }

    @Override
    public boolean hasCost(MPlayer player) {
        return player.hasCurrency(currencyType, amount);
    }

    @Override
    public boolean transaction(MPlayer player) {
        return player.takeCurrency(currencyType, amount);
    }

    @Override
    public String getName() {
        return currencyType.getDisplayName();
    }

    @Override
    public String getPriceline() {
        return currencyType.getDisplayName() + " " + formatNumberWithCommas(amount) + " " + currencyType.getSymbol();
    }

    @Override
    public String getInsufficientLine(MPlayer player) {
        return "&cInsufficient "
                + Color.strip(currencyType.getDisplayName())
                + ": &e" + Color.strip(currencyType.getSymbol())
                + " " + formatNumberWithCommas(player.getEconomy().get(currencyType))
                + "&7/&7&o" + formatNumberWithCommas(getAmount());
    }

    @Override
    public Cost combine(Cost similarCost) {
        double amt = ((CostCurrency) similarCost).getAmount();
        return new CostCurrency(currencyType, amt + getAmount());
    }

    public double getAmount() {
        return amount;
    }

    public static String formatNumberWithCommas(double number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(number);
    }

    @Override
    public String toString() {
        return "CostCurrency{" +
                "currencyType=" + currencyType +
                ", amount=" + amount +
                '}';
    }


}
