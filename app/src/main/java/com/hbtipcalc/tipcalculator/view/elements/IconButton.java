package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.graphics.drawable.DrawableCompat;

import com.hbtipcalc.tipcalculator.models.CalculatorApp;
import com.hbtipcalc.tipcalculator.models.CTheme;

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
        setScaleType(ScaleType.FIT_CENTER);

        int size = ((CalculatorApp) ctx.getApplicationContext()).getScreenProfile().getIconBtnSize();
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
