package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hbtipcalc.tipcalculator.models.CTheme;
import com.hbtipcalc.tipcalculator.models.CalculatorApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom dropdown component for displaying and selecting items from a list.
 * Features a collapsible popup menu with themed styling and observer pattern
 * for selection change notifications.
 */
public class DropDown extends LinearLayout
{
    private final List<DropDownObserver> observers;
    private final List<String> items;
    private final CTheme theme;
    private final TextView selectedView;
    private final TextView arrowView;
    private final LinearLayout selectedContainer;
    private PopupWindow popupWindow;
    private int selectedPosition = 0;

    /**
     * Constructs a new DropDown component.
     *
     * @param ctx The application context
     */
    public DropDown(Context ctx)
    {
        super(ctx);

        this.observers = new ArrayList<>();
        this.items = new ArrayList<>();

        CalculatorApp app = (CalculatorApp) ctx.getApplicationContext();
        this.theme = app.getCTheme();

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        ));

        // create a horizontal container for the text and arrow
        selectedContainer = new LinearLayout(ctx);
        selectedContainer.setOrientation(HORIZONTAL);
        selectedContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        ));
        selectedContainer.setBackgroundColor(theme.getBackgroundSecColor());
        selectedContainer.setPadding(20, 15, 20, 15);
        selectedContainer.setGravity(Gravity.CENTER_VERTICAL);

        // text view for selected item
        selectedView = new TextView(ctx);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                0,
                LayoutParams.WRAP_CONTENT,
                1f // takes up remaining space
        );
        selectedView.setLayoutParams(textParams);
        selectedView.setTextColor(theme.getTextColor());
        selectedView.setTextSize(theme.getTextFontSize());
        selectedView.setTypeface(theme.getFont());
        selectedView.setGravity(Gravity.CENTER_VERTICAL);
        selectedView.setText("Select...");

        // arrow view
        arrowView = new TextView(ctx);
        arrowView.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        ));
        arrowView.setTextColor(theme.getTextColor());
        arrowView.setTextSize(theme.getTextFontSize());
        arrowView.setText("▼");
        arrowView.setPadding(10, 0, 0, 0);

        selectedContainer.addView(selectedView);
        selectedContainer.addView(arrowView);

        selectedContainer.setOnClickListener(v -> toggleDropdown());

        addView(selectedContainer);
    }

    /**
     * Creates a view for an individual dropdown item.
     *
     * @param context The application context
     * @param itemText The text to display for this item
     * @param position The position of this item in the list
     * @param isSelected Whether this item is currently selected
     * @return A configured View for the dropdown item
     */
    protected View createItemView(Context context, String itemText, int position, boolean isSelected)
    {
        TextView itemView = new TextView(context);
        itemView.setText(itemText);
        itemView.setTextColor(theme.getTextColor());
        itemView.setTextSize(theme.getTextFontSize());
        itemView.setTypeface(theme.getFont());
        itemView.setPadding(20, 20, 20, 20);
        itemView.setBackgroundColor(theme.getBackgroundSecColor());

        if (isSelected)
        {
            itemView.setBackgroundColor(theme.getAccentColor());
            itemView.setTextColor(theme.getBackgroundColor());
        }

        return itemView;
    }

    /**
     * Toggles the dropdown menu between open and closed states.
     * Updates the arrow indicator accordingly.
     */
    private void toggleDropdown()
    {
        if (popupWindow != null && popupWindow.isShowing())
        {
            popupWindow.dismiss();
            arrowView.setText("▼");
            return;
        }

        arrowView.setText("▲");
        showDropdown();
    }

    /**
     * Displays the dropdown popup menu with all available items.
     * The popup is positioned below the selection container and includes
     * rounded corners, themed styling, and scrolling for long lists.
     */
    private void showDropdown()
    {
        LinearLayout dropdownLayout = new LinearLayout(getContext());
        dropdownLayout.setOrientation(VERTICAL);
        dropdownLayout.setBackgroundColor(theme.getBackgroundSecColor());
        dropdownLayout.setPadding(dpToPx(2), dpToPx(2), dpToPx(2), dpToPx(2));

        // round corners with border
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(theme.getBackgroundSecColor());
        drawable.setCornerRadii(new float[]{0, 0, 0, 0, 20, 20, 20, 20});
        drawable.setStroke(dpToPx(3), theme.getAccentColor());
        dropdownLayout.setBackground(drawable);
        dropdownLayout.setClipToOutline(true);

        for (int i = 0; i < items.size(); i++)
        {
            final int position = i;
            boolean isSelected = position == selectedPosition;

            View itemView = createItemView(getContext(), items.get(i), position, isSelected);

            // round corners for the last item to match the outline
            if (i == items.size() - 1)
            {
                GradientDrawable itemDrawable = new GradientDrawable();
                itemDrawable.setColor(isSelected ? theme.getAccentColor() : theme.getBackgroundSecColor());
                itemDrawable.setCornerRadii(new float[]{0, 0, 0, 0, 20, 20, 20, 20});
                itemView.setBackground(itemDrawable);
            }

            itemView.setOnClickListener(v -> {
                setSelection(position);
                popupWindow.dismiss();
                arrowView.setText("▼");
            });

            dropdownLayout.addView(itemView);
        }

        // wrap in ScrollView for scrolling
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.addView(dropdownLayout);
        scrollView.setVerticalScrollBarEnabled(true);
        scrollView.setScrollbarFadingEnabled(false);

        // calculate max height
        android.util.DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int screenHeight = displayMetrics.heightPixels;

        // simple max height - 40% of screen
        int maxHeight = (int)(screenHeight * 0.4);
        maxHeight = Math.max(maxHeight, dpToPx(150));

        int width = selectedContainer.getWidth();

        // create popup window with scrollable content
        popupWindow = new PopupWindow(
                scrollView,
                width,
                maxHeight,
                true
        );
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        // dismiss listener to reset arrow
        popupWindow.setOnDismissListener(() -> arrowView.setText("▼"));

        popupWindow.showAsDropDown(selectedContainer, 0, 15);
    }

    /**
     * Sets the items to display in the dropdown from an array.
     * If items exist and a valid selection is set, updates the display.
     *
     * @param items The array of items to display
     */
    public void setItems(String[] items)
    {
        this.items.clear();
        for (String item : items)
        {
            this.items.add(item);
        }

        if (items.length > 0 && selectedPosition < items.length)
        {
            selectedView.setText(items[selectedPosition]);
        }
    }

    /**
     * Sets the currently selected item by position.
     * Notifies all registered observers of the selection change.
     *
     * @param position The index of the item to select
     */
    public void setSelection(int position)
    {
        if (position >= 0 && position < items.size())
        {
            selectedPosition = position;
            selectedView.setText(items.get(position));
            notifyObservers(position, items.get(position));
        }
    }

    /**
     * Notifies all registered observers of a selection change.
     *
     * @param position The position of the newly selected item
     * @param value The value of the newly selected item
     */
    private void notifyObservers(int position, String value)
    {
        for (DropDownObserver observer : observers)
        {
            observer.handleDropDownChange(position, value);
        }
    }

    /**
     * Registers an observer to be notified of selection changes.
     * Prevents duplicate registrations.
     *
     * @param obs The observer to add
     */
    public void addObserver(DropDownObserver obs)
    {
        if (obs != null && !observers.contains(obs))
        {
            observers.add(obs);
        }
    }

    /**
     * Unregisters an observer from selection change notifications.
     *
     * @param obs The observer to remove
     */
    public void removeObserver(DropDownObserver obs)
    {
        observers.remove(obs);
    }

    /**
     * Converts density-independent pixels (dp) to actual pixels (px).
     *
     * @param dp The value in dp to convert
     * @return The equivalent value in pixels
     */
    private int dpToPx(int dp)
    {
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}