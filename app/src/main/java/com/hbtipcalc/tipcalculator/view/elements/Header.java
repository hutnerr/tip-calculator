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

import com.hbtipcalc.tipcalculator.models.CTheme;
import com.hbtipcalc.tipcalculator.models.CalculatorApp;

/**
 * This is the top navbar on any given page.
 */
public class Header extends LinearLayout {
    private final TextView titleView;
    private final LinearLayout buttonContainer;

    /**
     * Constructor. No title text.
     *
     * @param ctx The app context
     */
    public Header(Context ctx)
    {
        this(ctx, "");
    }

    /**
     * Constructor.
     *
     * @param ctx The app context
     * @param title The text to use for the title
     */
    public Header(Context ctx, String title)
    {
        super(ctx);

        CalculatorApp app = (CalculatorApp) ctx.getApplicationContext();
        CTheme t = app.getCTheme();

        setOrientation(HORIZONTAL);
        setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        setPadding(20, 20, 20, 20);
        setBackgroundColor(t.getAccentColor());
        setGravity(Gravity.CENTER_VERTICAL); // centered within the container

        // the title bar for the header itself
        titleView = new TextView(ctx);
        titleView.setText(title);
        titleView.setTextSize(t.getTileFontSize());
        titleView.setTextColor(t.getHeaderColor());
        titleView.setTypeface(t.getFont());
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

    /**
     * Adds an icon button to the header. Typically a settings cog etc. The will be added
     * to the right of the header text
     *
     * @param button The IconButton to add
     */
    public void addIconButton(IconButton button)
    {
        buttonContainer.addView(button);
    }

    // Setters
    public void setTitle(String title)
    {
        titleView.setText(title);
    }
}
