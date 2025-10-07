package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.graphics.drawable.DrawableCompat;

import com.hbtipcalc.tipcalculator.models.CTheme;
import com.hbtipcalc.tipcalculator.models.CalculatorApp;

/**
 * This is a simple button which takes in an SVG as its visuals.
 */
public class IconButton extends AppCompatImageButton
{
    /**
     * Constructor.
     *
     * @param ctx The app context
     */
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

    /**
     * Constructor which takes in an icon directly.
     *
     * @param ctx The app context
     * @param resID The resource ID of the icon
     */
    public IconButton(Context ctx, int resID)
    {
        this(ctx);
        setIcon(resID);
    }

    /**
     * Sets the icon of the button
     *
     * @param resID
     */
    public void setIcon(int resID)
    {
        setImageResource(resID);
        setIconColor(((CalculatorApp) getContext().getApplicationContext()).getCTheme().getHeaderColor());
    }

    /**
     * Sets the color of the icon.
     *
     * @param color
     */
    public void setIconColor(int color)
    {
        if (getDrawable() != null) {
            DrawableCompat.setTint(getDrawable(), color);
        }
    }
}
