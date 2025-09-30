package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;
import android.graphics.Paint;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbtipcalc.tipcalculator.styles.StyleConstants;

public class KeyValueText extends LinearLayout
{
    private TextView label;
    private TextView value;

    public KeyValueText(Context ctx)
    {
        this(ctx, "NULL", "NULL", false);
    }

    public KeyValueText(Context ctx, String labelText, String initialValue, boolean stretch)
    {
        super(ctx);
        setOrientation(LinearLayout.HORIZONTAL); // we want Key: Value (or similar)
        setPadding(60, 20, 60, 10);

        this.label = new TextView(ctx);
        this.label.setText(labelText);
        this.label.setTextColor(StyleConstants.COLOR_ACCENT);
        this.label.setTextSize(StyleConstants.FONT_TEXT);
        this.label.setTypeface(StyleConstants.MONOSPACE);

        this.value = new TextView(ctx);
        this.value.setText(initialValue);
        this.value.setTextColor(StyleConstants.COLOR_TEXT);
        this.value.setTextSize(StyleConstants.FONT_TITLE);
        this.value.setTypeface(StyleConstants.MONOSPACE);

        if (stretch)
        {
            LinearLayout.LayoutParams labelParams =
                    new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f);
            this.label.setLayoutParams(labelParams);

            LinearLayout.LayoutParams valueParams =
                    new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            this.value.setLayoutParams(valueParams);
        }

        addView(this.label);
        addView(this.value);
    }

    public TextView getValueLabel() { return this.value; }

    public void setValueText(String newValue)
    {
        this.value.setText(newValue);
    }
}
