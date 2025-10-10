package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;

import com.hbtipcalc.tipcalculator.controllers.CalculatorObserver;
import com.hbtipcalc.tipcalculator.models.TipResult;

public class SplitKeyValueText extends KeyValueText implements CalculatorObserver
{
    public SplitKeyValueText(Context ctx)
    {
        super(ctx, "Split Amount", "$0.00", true);
    }

    @Override
    public void handleTipCalculation(TipResult result)
    {
        // TODO: needs to get the actual tip
        // somehow get the slider value.
        // then update it
        // maybe alongside the calculator, i have a splitter controller which
        // i can use as a mediator
        setValueText(result.getFormattedTip());
    }
}
