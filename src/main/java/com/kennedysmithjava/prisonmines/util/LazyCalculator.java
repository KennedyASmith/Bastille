package com.kennedysmithjava.prisonmines.util;

import java.util.function.Supplier;

public class LazyCalculator<T> {

    private final long timeBetweenCalculation;
    private final Supplier<T> calculator;
    private long lastCalculationTime;

    public LazyCalculator(long timeBetweenCalculation, Supplier<T> calculator) {
        this.timeBetweenCalculation = timeBetweenCalculation;
        this.calculator = calculator;
        this.lastCalculationTime = 0;
    }

    private T previousResult;

    public T get() {
        if (System.currentTimeMillis() < timeBetweenCalculation + lastCalculationTime) {
            return this.previousResult;
        }

        this.previousResult = calculator.get();
        this.lastCalculationTime = System.currentTimeMillis();
        return this.previousResult;
    }

}

