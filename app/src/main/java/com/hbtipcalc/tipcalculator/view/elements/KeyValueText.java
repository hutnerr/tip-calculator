package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbtipcalc.tipcalculator.models.CTheme;
import com.hbtipcalc.tipcalculator.models.CalculatorApp;

public class KeyValueText extends LinearLayout
{
    private final TextView label;
    private final TextView value;

    public KeyValueText(Context ctx)
    {
        this(ctx, "NULL", "NULL", false);
    }

    public KeyValueText(Context ctx, String labelText, String initialValue, boolean stretch)
    {
        super(ctx);

        CalculatorApp app = (CalculatorApp) ctx.getApplicationContext();
        CTheme t = app.getCTheme();

        setOrientation(LinearLayout.HORIZONTAL); // we want Key: Value (or similar)
        setPadding(60, 20, 60, 10);

        this.label = new TextView(ctx);
        this.label.setText(labelText);
        this.label.setTextColor(t.getAccentColor());
        this.label.setTextSize(t.getTextFontSize());
        this.label.setTypeface(t.getFont());

        this.value = new TextView(ctx);
        this.value.setText(initialValue);
        this.value.setTextColor(t.getTextColor());
        this.value.setTextSize(t.getTileFontSize());
        this.value.setTypeface(t.getFont());

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
