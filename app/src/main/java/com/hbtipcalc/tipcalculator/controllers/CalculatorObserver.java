package com.hbtipcalc.tipcalculator.controllers;

import com.hbtipcalc.tipcalculator.models.TipResult;

/**
 * Interface for handling new Tip Calculations.
 */
public interface CalculatorObserver
{
    /**
     * Provides a new Tip Calculation.
     *
     * @param result The TipResult object of the new TIp
     */
    public void handleTipCalculation(TipResult result);
}
