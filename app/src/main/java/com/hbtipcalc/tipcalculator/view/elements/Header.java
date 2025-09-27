package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbtipcalc.tipcalculator.styles.StyleConstants;

public class Header extends LinearLayout {
    private TextView titleView;
    private LinearLayout buttonContainer;

    public Header(Context ctx, String title)
    {
        super(ctx);

        setOrientation(HORIZONTAL);
        setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        setPadding(20, 20, 20, 20);
        setBackgroundColor(StyleConstants.COLOR_ACCENT);
        setGravity(Gravity.CENTER_VERTICAL);

        titleView = new TextView(ctx);
        titleView.setText(title);
        titleView.setTextSize(StyleConstants.FONT_TITLE);
        titleView.setTextColor(StyleConstants.COLOR_TEXT);
        titleView.setTypeface(StyleConstants.MONOSPACE);

        LayoutParams titleParams = new LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
        );
        titleView.setLayoutParams(titleParams);

        // rightContainer for the right buttons
        buttonContainer = new LinearLayout(ctx);
        buttonContainer.setOrientation(HORIZONTAL);
        buttonContainer.setGravity(Gravity.END);

        addView(titleView);
        addView(buttonContainer);
    }

    /**
     * Sets the title of the Header.
     *
     * @param title The new title
     */
    public void setTitle(String title)
    {
        titleView.setText(title);
    }

    /**
     * Adds an icon Button to the header
     *
     * @param button The button to add
     */
    public void addIconButton(IconButton button)
    {
        buttonContainer.addView(button);
    }
}
