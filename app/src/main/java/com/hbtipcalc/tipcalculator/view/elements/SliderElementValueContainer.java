package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;
import android.view.View;

public class SliderElementValueContainer extends ElementValueContainer implements SliderObserver
{
    public SliderElementValueContainer(Context ctx, String labelText, View content)
    {
        this(ctx, labelText, content, "");
    }

    public SliderElementValueContainer(Context ctx, String labelText, View content, String valueText)
    {
        super(ctx, labelText, content, valueText);
    }

    @Override
    public void handleSliderChange(int newProgress)
    {
        setValue(newProgress + "%");
    }
}
