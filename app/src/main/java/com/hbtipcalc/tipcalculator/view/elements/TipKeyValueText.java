package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;

import com.hbtipcalc.tipcalculator.controllers.CalculatorObserver;
import com.hbtipcalc.tipcalculator.models.TipResult;

public class TipKeyValueText extends KeyValueText implements CalculatorObserver
{
    public TipKeyValueText(Context ctx)
    {
        super(ctx, "Tip Amount", "---", true);
    }

    @Override
    public void handleTipCalculation(TipResult result)
    {
        setValueText(result.getFormattedTip());
    }
}
