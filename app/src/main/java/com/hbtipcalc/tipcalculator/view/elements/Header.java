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
        setGravity(Gravity.CENTER_VERTICAL); // centered within the container

        // the title bar for the header itself
        titleView = new TextView(ctx);
        titleView.setText(title);
        titleView.setTextSize(StyleConstants.FONT_TITLE);
        titleView.setTextColor(StyleConstants.COLOR_HEADER);
        titleView.setTypeface(StyleConstants.MONOSPACE);
        LayoutParams titleParams = new LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
        );
        titleView.setLayoutParams(titleParams);

        // container for the icon buttons
        buttonContainer = new LinearLayout(ctx);
        buttonContainer.setOrientation(HORIZONTAL);
        buttonContainer.setGravity(Gravity.END);

        addView(titleView);
        addView(buttonContainer);
    }

    public void setTitle(String title)
    {
        titleView.setText(title);
    }

    public void addIconButton(IconButton button)
    {
        buttonContainer.addView(button);
    }
}
