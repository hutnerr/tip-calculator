package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbtipcalc.tipcalculator.models.CTheme;

import java.util.ArrayList;
import java.util.List;

public class ThemeDropDown extends DropDown
{
    private final List<CTheme> themes;

    public ThemeDropDown(Context ctx)
    {
        super(ctx);
        this.themes = new ArrayList<>();
    }

    /**
     * Set the available themes for the dropdown
     * @param themes List of CTheme enums to display
     */
    public void setThemes(List<CTheme> themes)
    {
        this.themes.clear();
        this.themes.addAll(themes);

        // Extract theme names for the parent class
        List<String> themeNames = new ArrayList<>();
        for (CTheme theme : themes)
        {
            themeNames.add(formatThemeName(theme.name()));
        }
        super.setItems(themeNames);
    }

    /**
     * Set themes from array
     */
    public void setThemes(CTheme[] themes)
    {
        this.themes.clear();
        List<String> themeNames = new ArrayList<>();
        for (CTheme theme : themes)
        {
            this.themes.add(theme);
            themeNames.add(formatThemeName(theme.name()));
        }
        super.setItems(themeNames);
    }

    /**
     * Get the currently selected theme
     * @return The selected CTheme enum
     */
    public CTheme getSelectedTheme()
    {
        int position = getSelectedPosition();
        if (position >= 0 && position < themes.size())
        {
            return themes.get(position);
        }
        return null;
    }

    /**
     * Set selection by theme enum
     * @param theme The CTheme to select
     */
    public void setSelectedTheme(CTheme theme)
    {
        int index = themes.indexOf(theme);
        if (index >= 0)
        {
            setSelection(index);
        }
    }

    /**
     * Override to create custom item views with color preview
     */
    @Override
    protected View createItemView(Context context, String itemText, int position, boolean isSelected)
    {
        LinearLayout itemContainer = new LinearLayout(context);
        itemContainer.setOrientation(LinearLayout.HORIZONTAL);
        itemContainer.setGravity(Gravity.CENTER_VERTICAL);
        itemContainer.setPadding(20, 20, 20, 20);

        CTheme theme = themes.get(position);
        CTheme currentTheme = ((com.hbtipcalc.tipcalculator.models.CalculatorApp)
                context.getApplicationContext()).getCTheme();

        // Set background color based on selection
        int bgColor = isSelected ? currentTheme.getAccentColor() : currentTheme.getBackgroundSecColor();
        itemContainer.setBackgroundColor(bgColor);

        // Theme name text view
        TextView textView = new TextView(context);
        textView.setText(itemText);
        textView.setTextColor(isSelected ? currentTheme.getBackgroundColor() : currentTheme.getTextColor());
        textView.setTextSize(currentTheme.getTextFontSize());
        textView.setTypeface(currentTheme.getFont());

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                0,
                LayoutParams.WRAP_CONTENT,
                1f
        );
        textView.setLayoutParams(textParams);
        itemContainer.addView(textView);

        // Color preview container
        LinearLayout colorPreview = createColorPreview(context, theme);
        itemContainer.addView(colorPreview);

        return itemContainer;
    }

    /**
     * Create a horizontal layout with 5 small colored boxes
     */
    private LinearLayout createColorPreview(Context context, CTheme theme)
    {
        LinearLayout colorContainer = new LinearLayout(context);
        colorContainer.setOrientation(LinearLayout.HORIZONTAL);
        colorContainer.setGravity(Gravity.CENTER_VERTICAL);

        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        containerParams.setMargins(10, 0, 0, 0);
        colorContainer.setLayoutParams(containerParams);

        // Create 5 color boxes
        int[] colors = {
                theme.getTextColor(),
                theme.getHeaderColor(),
                theme.getBackgroundColor(),
                theme.getBackgroundSecColor(),
                theme.getAccentColor()
        };

        int boxSize = 30; // Size in dp (will need to convert to px)
        int boxMargin = 4;

        for (int i = 0; i < colors.length; i++)
        {
            View colorBox = new View(context);

            // Create rounded rectangle drawable
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(colors[i]);
            drawable.setCornerRadius(6);
            drawable.setStroke(2, 0x40FFFFFF); // Semi-transparent white border
            colorBox.setBackground(drawable);

            LinearLayout.LayoutParams boxParams = new LinearLayout.LayoutParams(
                    dpToPx(context, boxSize),
                    dpToPx(context, boxSize)
            );

            if (i < colors.length - 1)
            {
                boxParams.setMargins(0, 0, dpToPx(context, boxMargin), 0);
            }

            colorBox.setLayoutParams(boxParams);
            colorContainer.addView(colorBox);
        }

        return colorContainer;
    }

    /**
     * Format theme name from UPPER_CASE to Title Case
     */
    private String formatThemeName(String name)
    {
        String[] words = name.split("_");
        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < words.length; i++)
        {
            String word = words[i];
            formatted.append(word.charAt(0))
                    .append(word.substring(1).toLowerCase());

            if (i < words.length - 1)
            {
                formatted.append(" ");
            }
        }

        return formatted.toString();
    }

    /**
     * Convert dp to pixels
     */
    private int dpToPx(Context context, int dp)
    {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}