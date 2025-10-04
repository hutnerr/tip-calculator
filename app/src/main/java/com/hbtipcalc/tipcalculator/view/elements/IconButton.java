package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.graphics.drawable.DrawableCompat;

import com.hbtipcalc.tipcalculator.models.CTheme;
import com.hbtipcalc.tipcalculator.models.CalculatorApp;

public class IconButton extends AppCompatImageButton
{
    public IconButton(Context ctx)
    {
        super(ctx);

        setBackground(null); // remove ripple/gray background
        setAdjustViewBounds(true);
        setScaleType(ScaleType.CENTER_INSIDE);

        // set to a static size
        int size = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 56, getResources().getDisplayMetrics()
        );
        setLayoutParams(new ViewGroup.LayoutParams(size, size));
    }

    public IconButton(Context ctx, int resID)
    {
        this(ctx);
        setIcon(resID);
    }

    public void setIcon(int resID)
    {
        setImageResource(resID);
        setIconColor(((CalculatorApp) getContext().getApplicationContext()).getCTheme().getHeaderColor());
    }

    public void setIconColor(int color)
    {
        if (getDrawable() != null) {
            DrawableCompat.setTint(getDrawable(), color);
        }
    }
}
