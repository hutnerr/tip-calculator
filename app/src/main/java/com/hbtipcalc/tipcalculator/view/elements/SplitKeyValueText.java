package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;

import com.hbtipcalc.tipcalculator.controllers.CalculatorObserver;
import com.hbtipcalc.tipcalculator.models.TipResult;

/**
 * Specialized KeyValueText component that displays the split amount per person.
 * Implements CalculatorObserver to automatically update when tip calculations change.
 */
public class SplitKeyValueText extends KeyValueText implements CalculatorObserver
{
    /**
     * Constructs a new SplitKeyValueText component.
     * Initializes with "Split Amount" label and placeholder value.
     *
     * @param ctx The application context
     */
    public SplitKeyValueText(Context ctx)
    {
        super(ctx, "Split Amount", "----", true);
    }

    /**
     * Handles tip calculation updates from the calculator.
     * Updates the displayed value with the formatted split amount per person.
     *
     * @param result The tip calculation result containing the formatted split amount
     */
    @Override
    public void handleTipCalculation(TipResult result)
    {
        setValueText(result.getFormattedSplit());
    }
}