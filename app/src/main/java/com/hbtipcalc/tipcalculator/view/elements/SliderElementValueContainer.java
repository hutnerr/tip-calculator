package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;
import android.view.View;

/**
 * This is an element value container which allows a slider to change its value.
 */
public class SliderElementValueContainer extends ElementValueContainer implements SliderObserver
{
    /**
     * Constructor
     *
     * @param ctx The context of the app
     * @param labelText The "title" label text
     * @param content The content
     */
    public SliderElementValueContainer(Context ctx, String labelText, View content)
    {
        this(ctx, labelText, content, "");
    }

    /**
     * Constructor
     *
     * @param ctx The context of the app
     * @param labelText The "title" label text
     * @param content The content
     * @param valueText The "value" label text
     */
    public SliderElementValueContainer(Context ctx, String labelText, View content, String valueText)
    {
        super(ctx, labelText, content, valueText);
    }

    @Override
    public void handleSliderChange(int newProgress)
    {
        // FIXME: currently hardcoded to work with percents. maybe store a postfix/postfix as state?
        setValue(newProgress + "%");
    }
}
