package com.hbtipcalc.tipcalculator.controllers;

import com.hbtipcalc.tipcalculator.math.TipCalculator;
import com.hbtipcalc.tipcalculator.math.TipResult;
import com.hbtipcalc.tipcalculator.models.RoundingFlag;
import com.hbtipcalc.tipcalculator.view.elements.SliderObserver;

import java.math.BigDecimal;
import java.util.List;

public class Calculator implements SliderObserver
{
    private BigDecimal billAmt;
    private int tipPercent;
    private RoundingFlag roundingFlag;
    private List<CalculatorObserver> observers;

    public Calculator(BigDecimal billAmt, int tipPercent, RoundingFlag roundingFlag)
    {
        this.billAmt = billAmt;
        this.tipPercent = tipPercent;
        this.roundingFlag = roundingFlag;
    }

    public void calculate()
    {
        TipResult result = TipCalculator.calculate(billAmt, tipPercent, roundingFlag);
        notifyObservers(result);
    }

    public void addObserver(CalculatorObserver observer)
    {
        this.observers.add(observer);
    }

    public void removeObserver(CalculatorObserver observer)
    {
        this.observers.remove(observer);
    }

    public void notifyObservers(TipResult result)
    {
        for (CalculatorObserver observer : observers)
        {
            observer.handleTipCalculation(result);
        }
    }

    @Override
    public void handleSliderChange(int newVal)
    {
        this.tipPercent = newVal;
        calculate();
    }

    // TODO: make a Textbox object which is just version of an EditText
    // then make the relevant observer.

    // will need to be a slider observer and a textbox observer. maybe i can generalize
    // the observer interface i have?
}
