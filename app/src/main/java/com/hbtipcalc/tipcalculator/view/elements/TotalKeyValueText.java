package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;

import com.hbtipcalc.tipcalculator.controllers.CalculatorObserver;
import com.hbtipcalc.tipcalculator.models.TipResult;

public class TotalKeyValueText extends KeyValueText implements CalculatorObserver
{
    public TotalKeyValueText(Context ctx)
    {
        super(ctx, "Total Amount", "$0.00", true);
    }

    @Override
    public void handleTipCalculation(TipResult result)
    {
        setValueText(result.getFormattedTotal());
    }
}
