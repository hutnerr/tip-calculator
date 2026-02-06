package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;

import com.hbtipcalc.tipcalculator.controllers.CalculatorObserver;
import com.hbtipcalc.tipcalculator.models.TipResult;

/**
 * Specialized KeyValueText component that displays the calculated tip amount.
 * Implements CalculatorObserver to automatically update when tip calculations change.
 */
public class TipKeyValueText extends KeyValueText implements CalculatorObserver
{
    /**
     * Constructs a new TipKeyValueText component.
     * Initializes with "Tip Amount" label and placeholder value.
     *
     * @param ctx The application context
     */
    public TipKeyValueText(Context ctx)
    {
        super(ctx, "Tip Amount", "---", true);
    }

    /**
     * Handles tip calculation updates from the calculator.
     * Updates the displayed value with the formatted tip amount.
     *
     * @param result The tip calculation result containing the formatted tip amount
     */
    @Override
    public void handleTipCalculation(TipResult result)
    {
        setValueText(result.getFormattedTip());
    }
}