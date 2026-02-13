package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import com.hbtipcalc.tipcalculator.models.CTheme;
import com.hbtipcalc.tipcalculator.models.CalculatorApp;

import java.util.ArrayList;
import java.util.List;

public class NumPad extends GridLayout
{
    public final static int CLEAR = -1;
    public final static int BACKSPACE = -2;

    private List<NumPadObserver> observers;
    private CTheme t;

    public NumPad(Context ctx)
    {
        this(ctx, false);
    }

    public NumPad(Context ctx, boolean inverted)
    {
        super(ctx);
        setColumnCount(3);
        setRowCount(4);
        setAlignmentMode(GridLayout.ALIGN_MARGINS);
        setUseDefaultMargins(true);
        setPadding(24, 50, 24, 12);

        this.observers = new ArrayList<>();

        String[] keys;
        if (inverted)
        {
            keys = new String[] {
                    "1", "2", "3",
                    "4", "5", "6",
                    "7", "8", "9",
                    "C", "0", "⌫"
            };
        }
        else
        {
            keys = new String[] {
                    "7", "8", "9",
                    "4", "5", "6",
                    "1", "2", "3",
                    "C", "0", "⌫"
            };
        }

        CalculatorApp app = (CalculatorApp) ctx.getApplicationContext();
        this.t = app.getCTheme();

        for (String key : keys)
        {
            Button btn = new Button(ctx);
            btn.setText(key);
            btn.setOnClickListener(onButtonClick);
            styleNumPadButton(btn);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(12, 12, 12, 12);

            btn.setLayoutParams(params);
            addView(btn);
        }

    }

    private void styleNumPadButton(Button btn)
    {
        int cornerRadius = 32;
        int borderColor = Color.parseColor("#CCCCCC");

        GradientDrawable normal = new GradientDrawable();
        normal.setShape(GradientDrawable.RECTANGLE);
        normal.setCornerRadius(cornerRadius);
        normal.setColor(t.getBackgroundSecColor());

        GradientDrawable pressed = new GradientDrawable();
        pressed.setCornerRadius(cornerRadius);
        pressed.setColor(adjustBrightness(t.getBackgroundSecColor(), 0.65f));

        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, pressed);
        states.addState(new int[]{}, normal);

        btn.setBackground(states);

        btn.setTextSize(t.getNumPadBtnFontSize());
        btn.setTextColor(t.getTextColor());
        btn.setPadding(0, 30, 0, 30);
    }

    private int adjustBrightness(int color, float factor)
    {
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.rgb(
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255)
        );
    }

    private final View.OnClickListener onButtonClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Button b = (Button) v;
            String text = b.getText().toString();
            int value;

            if (text.equals("C")) value = -1;
            else if (text.equals("⌫")) value = -2;
            else value = Integer.parseInt(text);
            notifyObservers(value);
        }
    };

    public void notifyObservers(int value)
    {
        for (NumPadObserver observer : observers)
        {
            observer.handleButtonPressed(value);
        }
    }

    public void addObserver(NumPadObserver observer)
    {
        observers.add(observer);
    }
    public void removeObserver(NumPadObserver observer)
    {
        observers.remove(observer);
    }
}
