package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ElementValueContainer but also provides an additional area besides the "Title" label
 * to display more information.
 */
public class ElementValueContainer extends ElementContainer
{
    private final TextView valueView;

    /**
     * Default constructor. Provides no text for the new value. Looks the same as its parent.
     *
     * @param ctx App Context
     * @param labelText The label of the title
     * @param content The content?
     */
    public ElementValueContainer(Context ctx, String labelText, View content)
    {
        this(ctx, labelText, content, "");
    }

    /**
     * Constructor
     *
     * @param ctx App Context
     * @param labelText The label of the title
     * @param content The content?
     * @param valueText The text for the new text area
     */
    public ElementValueContainer(Context ctx, String labelText, View content, String valueText)
    {
        super(ctx, labelText, content);

        // right label: this is the "value"
        valueView = new TextView(ctx);
        valueView.setText(valueText);
        valueView.setTextSize(t.getTextFontSize());
        valueView.setTextColor(t.getAccentColor());
        valueView.setTypeface(t.getFont());
        valueView.setGravity(Gravity.END); // we want to at the right side of the container
        valueView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        labelContainer.addView(valueView);
    }

    public String getValue() { return valueView.getText().toString(); }
    public void setValue(String text) { valueView.setText(text); }
}
