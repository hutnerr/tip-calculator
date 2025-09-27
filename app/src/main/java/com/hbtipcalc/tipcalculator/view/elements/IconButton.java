package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.graphics.drawable.DrawableCompat;

import com.hbtipcalc.tipcalculator.styles.StyleConstants;

public class IconButton extends AppCompatImageButton
{
    public IconButton(Context context) {
        super(context);

        setBackground(null); // remove ripple/gray background
        setAdjustViewBounds(true);
        setScaleType(ScaleType.CENTER_INSIDE);

        // Force standard touch size (48dp square)
        int size = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 56, getResources().getDisplayMetrics()
        );
        setLayoutParams(new ViewGroup.LayoutParams(size, size));
    }

    public IconButton(Context context, int resID)
    {
        this(context);
        setIcon(resID);
    }

    public void setIcon(int resID)
    {
        setImageResource(resID);
        setIconColor(StyleConstants.COLOR_TEXT);
    }

    public void setIconColor(int color)
    {
        if (getDrawable() != null) {
            DrawableCompat.setTint(getDrawable(), color);
        }
    }
}
