package com.hbtipcalc.tipcalculator.controllers;

import com.hbtipcalc.tipcalculator.math.TipResult;

public interface CalculatorObserver
{
    public void handleTipCalculation(TipResult result);
}
