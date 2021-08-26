package com.kennedysmithjava.prisonmines.blockhandler;

public interface ValueMutable extends Valuable {

    void setValue(double amount);

    default void multiplyValue(double multiplier) {
        this.setValue(this.getValue() * multiplier);
    }

    @Override
    default ValueMutable getMutable() {
        return this;
    }
}
