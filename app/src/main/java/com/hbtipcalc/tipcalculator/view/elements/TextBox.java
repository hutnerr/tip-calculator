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

public class TextBox extends androidx.appcompat.widget.AppCompatTextView implements NumPadObserver
{
    private static final int MAX_DIGITS = 9;
    private double value;
    private DecimalFormat formatter;
    private CTheme t;
    private List<TextBoxObserver> observers;

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
        setGravity(Gravity.END);
        setPadding(30, 20, 30, 20);
        setBackgroundColor(t.getBackgroundSecColor());
    }

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

    public void backspace()
    {
        long cents = Math.round(value * 100);
        cents /= 10;
        value = cents / 100.0;
        updateText();
    }

    public void clear()
    {
        value = 0.0;
        updateText();
    }

    public void setValue(double newValue)
    {
        value = newValue;
        updateText();
    }

    public double getValue()
    {
        return value;
    }

    private void updateText()
    {
        setText(formatter.format(value));
    }

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

    public void notifyObservers(String text)
    {
        for (TextBoxObserver observer : observers)
        {
            observer.handleText(text);
        }
    }

    public void addObserver(TextBoxObserver observer)
    {
        observers.add(observer);
    }

    public void removeObserver(TextBoxObserver observer)
    {
        observers.remove(observer);
    }
}
