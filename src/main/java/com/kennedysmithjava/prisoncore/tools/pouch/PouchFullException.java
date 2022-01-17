package com.kennedysmithjava.prisoncore.tools.pouch;

public class PouchFullException extends Exception {

    private final int amountRemaining;

    public PouchFullException(int amount) {
        super();
        this.amountRemaining = amount;
    }

    public PouchFullException() {
        this(1);
    }

    public int getAmountRemaining() {
        return amountRemaining;
    }
}
