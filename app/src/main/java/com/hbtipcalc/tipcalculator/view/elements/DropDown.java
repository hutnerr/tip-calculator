package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hbtipcalc.tipcalculator.models.CTheme;
import com.hbtipcalc.tipcalculator.models.CalculatorApp;

import java.util.ArrayList;
import java.util.List;

public class DropDown extends LinearLayout
{
    private final List<DropDownObserver> observers;
    private final List<String> items;
    private final CTheme theme;
    private final TextView selectedView;
    private PopupWindow popupWindow;
    private int selectedPosition = 0;

    public DropDown(Context ctx)
    {
        super(ctx);

        this.observers = new ArrayList<>();
        this.items = new ArrayList<>();

        CalculatorApp app = (CalculatorApp) ctx.getApplicationContext();
        this.theme = app.getCTheme();

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);

        selectedView = new TextView(ctx);
        selectedView.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        ));
        selectedView.setBackgroundColor(theme.getBackgroundSecColor());
        selectedView.setTextColor(theme.getTextColor());
        selectedView.setTextSize(theme.getTextFontSize());
        selectedView.setTypeface(theme.getFont());
        selectedView.setPadding(20, 35, 20, 35);
        selectedView.setGravity(Gravity.CENTER_VERTICAL);
        selectedView.setText("Select...");

        selectedView.setOnClickListener(v -> toggleDropdown());

        addView(selectedView);
    }

    private void toggleDropdown()
    {
        if (popupWindow != null && popupWindow.isShowing())
        {
            popupWindow.dismiss();
            return;
        }

        showDropdown();
    }

    private void showDropdown()
    {
        LinearLayout dropdownLayout = new LinearLayout(getContext());
        dropdownLayout.setOrientation(VERTICAL);
        dropdownLayout.setBackgroundColor(theme.getBackgroundSecColor());

        // round corners
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(theme.getBackgroundSecColor());
        drawable.setCornerRadii(new float[]{0, 0, 0, 0, 20, 20, 20, 20});
        dropdownLayout.setBackground(drawable);
        dropdownLayout.setClipToOutline(true);

        // load the items
        for (int i = 0; i < items.size(); i++)
        {
            final int position = i;
            TextView itemView = new TextView(getContext());
            itemView.setText(items.get(i));
            itemView.setTextColor(theme.getTextColor());
            itemView.setTextSize(theme.getTextFontSize());
            itemView.setTypeface(theme.getFont());
            itemView.setPadding(20, 20, 20, 20);
            itemView.setBackgroundColor(theme.getBackgroundSecColor());

            // selected item should be look dif
            if (position == selectedPosition)
            {
                itemView.setBackgroundColor(theme.getAccentColor());
                itemView.setTextColor(theme.getBackgroundColor());
            }

            // round corners for the last item to match the outline
            if (i == items.size() - 1)
            {
                GradientDrawable itemDrawable = new GradientDrawable();
                itemDrawable.setColor(position == selectedPosition ? theme.getAccentColor() : theme.getBackgroundSecColor());
                itemDrawable.setCornerRadii(new float[]{0, 0, 0, 0, 20, 20, 20, 20});
                itemView.setBackground(itemDrawable);
            }

            itemView.setOnClickListener(v -> {
                setSelection(position);
                popupWindow.dismiss();
            });

            dropdownLayout.addView(itemView);
        }

        int width = this.getWidth();

        // make the same width as parent
        popupWindow = new PopupWindow(
                dropdownLayout,
                width,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        // show below selected view
        popupWindow.showAsDropDown(selectedView, 0, 15);
    }

    public void setItems(List<String> items)
    {
        this.items.clear();
        this.items.addAll(items);

        if (!items.isEmpty() && selectedPosition < items.size())
        {
            selectedView.setText(items.get(selectedPosition));
        }
    }

    public void setItems(String[] items)
    {
        this.items.clear();
        for (String item : items) {
            this.items.add(item);
        }

        if (items.length > 0 && selectedPosition < items.length)
        {
            selectedView.setText(items[selectedPosition]);
        }
    }

    public void setSelection(int position)
    {
        if (position >= 0 && position < items.size())
        {
            selectedPosition = position;
            selectedView.setText(items.get(position));
            notifyObservers(position, items.get(position));
        }
    }

    public void setSelection(String value)
    {
        int index = items.indexOf(value);
        if (index >= 0)
        {
            setSelection(index);
        }
    }

    public int getSelectedPosition() { return selectedPosition; }
    public String getSelectedItem() { return selectedPosition >= 0 && selectedPosition < items.size() ? items.get(selectedPosition) : null; }

    private void notifyObservers(int position, String value)
    {
        for (DropDownObserver observer : observers)
        {
            observer.handleDropDownChange(position, value);
        }
    }

    public void addObserver(DropDownObserver obs)
    {
        if (obs != null && !observers.contains(obs)) observers.add(obs);
    }

    public void removeObserver(DropDownObserver obs)
    {
        observers.remove(obs);
    }
}