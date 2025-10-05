package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbtipcalc.tipcalculator.MainActivity;
import com.hbtipcalc.tipcalculator.models.CTheme;
import com.hbtipcalc.tipcalculator.models.CalculatorApp;
import com.hbtipcalc.tipcalculator.models.Settings;

public class ElementContainer extends LinearLayout
{
    private final LinearLayout contentContainer;
    protected final LinearLayout labelContainer;
    private final TextView labelView;
    protected final CTheme t;

    public ElementContainer(Context ctx, String labelText, View content)
    {
        super(ctx);

        CalculatorApp app = (CalculatorApp) ctx.getApplicationContext();
        this.t = app.getCTheme();

        setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setPadding(60, 40, 60, 20);

        // layout for the top labels
        this.labelContainer = new LinearLayout(ctx);
        labelContainer.setOrientation(HORIZONTAL);
        labelContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        // left label: this is the "title"
        labelView = new TextView(ctx);
        labelView.setText(labelText);
        labelView.setTextSize(t.getTextFontSize());
        labelView.setTextColor(t.getAccentColor());
        labelView.setTypeface(t.getFont());
        labelView.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));

        labelContainer.addView(labelView);

        // create the container which houses the actual element inside
        contentContainer = new LinearLayout(ctx);
        contentContainer.setOrientation(VERTICAL);
        contentContainer.setPadding(20, 20, 20, 20);
        contentContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        GradientDrawable border = new GradientDrawable();
        border.setColor(t.getBackgroundSecColor()); // make it invis by same as the bg
        border.setCornerRadius(32);
        contentContainer.setBackground(border);

        contentContainer.addView(content);

        addView(labelContainer);
        addView(contentContainer);
    }

    public void setLabel(String text) { labelView.setText(text); }
    public View getContent() { return contentContainer.getChildAt(0);}
}
