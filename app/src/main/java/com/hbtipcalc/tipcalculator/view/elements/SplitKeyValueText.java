package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;

import com.hbtipcalc.tipcalculator.controllers.CalculatorObserver;
import com.hbtipcalc.tipcalculator.models.TipResult;

public class SplitKeyValueText extends KeyValueText implements CalculatorObserver
{
    public SplitKeyValueText(Context ctx)
    {
        super(ctx, "Split Amount", "----", true);
    }

    @Override
    public void handleTipCalculation(TipResult result)
    {
        setValueText(result.getFormattedSplit());
    }
}
