package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;
import android.view.View;

/**
 * This is an element value container which allows a slider to change its value.
 */
public class SliderElementValueContainer extends ElementValueContainer implements SliderObserver
{
    private String postfix;

    /**
     * Constructor
     *
     * @param ctx The context of the app
     * @param labelText The "title" label text
     * @param postfix The text to appear after the slider value
     * @param content The content
     */
    public SliderElementValueContainer(Context ctx, String labelText, View content, String postfix)
    {
        this(ctx, labelText, content, postfix,"");
    }

    /**
     * Constructor
     *
     * @param ctx The context of the app
     * @param labelText The "title" label text
     * @param content The content
     * @param postfix The text to appear after the slider value
     * @param valueText The "value" label text
     */
    public SliderElementValueContainer(Context ctx, String labelText, View content, String postfix, String valueText)
    {
        super(ctx, labelText, content, valueText);
        this.postfix = postfix;
    }

    @Override
    public void handleSliderChange(int newProgress)
    {
        setValue(newProgress + postfix);
    }
}
