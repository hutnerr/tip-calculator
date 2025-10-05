package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ElementValueContainer extends ElementContainer
{
    private final TextView valueView;

    public ElementValueContainer(Context ctx, String labelText, View content)
    {
        this(ctx, labelText, content, "");
    }

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
