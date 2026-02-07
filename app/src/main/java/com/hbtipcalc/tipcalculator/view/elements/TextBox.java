package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.hbtipcalc.tipcalculator.models.CTheme;
import com.hbtipcalc.tipcalculator.models.CalculatorApp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom text box component for displaying and editing currency values.
 * Implements NumPadObserver to receive input from a NumPad component.
 * Supports digit-by-digit entry with automatic decimal formatting and
 * observer pattern for value change notifications.
 */
public class TextBox extends androidx.appcompat.widget.AppCompatTextView implements NumPadObserver
{
    private static final int MAX_DIGITS = 9;
    private double value;
    private DecimalFormat formatter;
    private CTheme t;
    private List<TextBoxObserver> observers;

    /**
     * Constructs a new TextBox component.
     * Initializes with currency formatting from app settings.
     *
     * @param ctx The application context
     */
    public TextBox(Context ctx)
    {
        super(ctx);
        this.observers = new ArrayList<>();

        CalculatorApp app = (CalculatorApp) ctx.getApplicationContext();
        this.t = app.getCTheme();

        String c = app.getSettings().getCurrency();
        this.formatter = new DecimalFormat(c + "0.00");

        value = 0.0;
        setText(formatter.format(value));

        setTextSize(t.getTileFontSize());
        setTextColor(t.getTextColor());
        setTypeface(t.getFont());
        setGravity(Gravity.END);
        setPadding(30, 10, 30, 10);
        setBackgroundColor(t.getBackgroundSecColor());
    }

    /**
     * Appends a digit to the current value.
     * Digits are added from right to left with decimal handling.
     * Flashes red if maximum digit limit is reached.
     *
     * @param digit The digit to append (0-9)
     */
    public void appendDigit(int digit)
    {
        long cents = Math.round(value * 100);
        String digits = String.valueOf(cents);
        if (digits.length() >= MAX_DIGITS)
        {
            setTextColor(Color.RED);
            postDelayed(() -> setTextColor(t.getTextColor()), 400);
            return;
        }

        value = value * 10 + (digit / 100.0);
        updateText();
    }

    /**
     * Removes the rightmost digit from the current value.
     * Effectively divides the value by 10 in cents.
     */
    public void backspace()
    {
        long cents = Math.round(value * 100);
        cents /= 10;
        value = cents / 100.0;
        updateText();
    }

    /**
     * Resets the value to zero.
     */
    public void clear()
    {
        value = 0.0;
        updateText();
    }

    /**
     * Sets the value directly.
     *
     * @param newValue The new value to set
     */
    public void setValue(double newValue)
    {
        value = newValue;
        updateText();
    }

    /**
     * Gets the current numeric value.
     *
     * @return The current value as a double
     */
    public double getValue()
    {
        return value;
    }

    /**
     * Updates the displayed text with the formatted value.
     */
    private void updateText()
    {
        setText(formatter.format(value));
    }

    /**
     * Handles button press events from the NumPad.
     * Processes digits, clear, and backspace commands.
     *
     * @param btnValue The button value (digit, CLEAR, or BACKSPACE)
     */
    @Override
    public void handleButtonPressed(int btnValue)
    {
        switch (btnValue)
        {
            case NumPad.CLEAR:
                clear();
                break;
            case NumPad.BACKSPACE:
                backspace();
                break;
            default:
                appendDigit(btnValue);
                break;
        }
        notifyObservers(getText().toString());
    }

    /**
     * Notifies all registered observers of a text change.
     *
     * @param text The new formatted text value
     */
    public void notifyObservers(String text)
    {
        for (TextBoxObserver observer : observers)
        {
            observer.handleText(text);
        }
    }

    /**
     * Registers an observer to be notified of text changes.
     *
     * @param observer The observer to add
     */
    public void addObserver(TextBoxObserver observer)
    {
        if (observer != null && !observers.contains(observer))
        {
            observers.add(observer);
        }
    }

    /**
     * Unregisters an observer from text change notifications.
     *
     * @param observer The observer to remove
     */
    public void removeObserver(TextBoxObserver observer)
    {
        observers.remove(observer);
    }
}