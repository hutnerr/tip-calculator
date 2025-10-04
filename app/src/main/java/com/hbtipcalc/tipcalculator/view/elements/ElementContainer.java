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

public class ElementContainer extends LinearLayout implements SliderObserver {

    private final LinearLayout contentContainer;
    private final TextView labelView;
    private final TextView valueView; // right-aligned label above container

    public ElementContainer(Context ctx, String labelText, View content)
    {
        super(ctx);

        CalculatorApp app = (CalculatorApp) ctx.getApplicationContext();
        CTheme t = app.getCTheme();

        setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setPadding(60, 40, 60, 20);

        // layout for the top labels
        LinearLayout topLabels = new LinearLayout(ctx);
        topLabels.setOrientation(HORIZONTAL);
        topLabels.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        // left label: this is the "title"
        labelView = new TextView(ctx);
        labelView.setText(labelText);
        labelView.setTextSize(t.getTextFontSize());
        labelView.setTextColor(t.getAccentColor());
        labelView.setTypeface(t.getFont());
        labelView.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));

        // right label: this is the "value"
        valueView = new TextView(ctx);
        valueView.setText(""); // nothing by default
        valueView.setTextSize(t.getTextFontSize());
        valueView.setTextColor(t.getAccentColor());
        valueView.setTypeface(t.getFont());
        valueView.setGravity(Gravity.END); // we want to at the right side of the container
        valueView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        topLabels.addView(labelView);
        topLabels.addView(valueView);

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

        addView(topLabels);
        addView(contentContainer);
    }

    @Override
    public void handleSliderChange(int newProgress)
    {
        setValue(newProgress + "%");
    }

    public void setLabel(String text) { labelView.setText(text); }
    public void setValue(String text) { valueView.setText(text); }

    public String getValue() { return valueView.getText().toString(); }
    public View getContent() { return contentContainer.getChildAt(0);}
}
