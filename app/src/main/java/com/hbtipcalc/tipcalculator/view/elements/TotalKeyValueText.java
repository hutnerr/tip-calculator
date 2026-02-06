package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;

import com.hbtipcalc.tipcalculator.controllers.CalculatorObserver;
import com.hbtipcalc.tipcalculator.models.TipResult;

/**
 * Specialized KeyValueText component that displays the calculated total amount.
 * Implements CalculatorObserver to automatically update when tip calculations change.
 */
public class TotalKeyValueText extends KeyValueText implements CalculatorObserver
{
    /**
     * Constructs a new TotalKeyValueText component.
     * Initializes with "Total Amount" label and placeholder value.
     *
     * @param ctx The application context
     */
    public TotalKeyValueText(Context ctx)
    {
        super(ctx, "Total Amount", "---", true);
    }

    /**
     * Handles tip calculation updates from the calculator.
     * Updates the displayed value with the formatted total amount.
     *
     * @param result The tip calculation result containing the formatted total amount
     */
    @Override
    public void handleTipCalculation(TipResult result)
    {
        setValueText(result.getFormattedTotal());
    }
}