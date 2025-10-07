package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import com.hbtipcalc.tipcalculator.R;
import com.hbtipcalc.tipcalculator.models.CTheme;
import com.hbtipcalc.tipcalculator.models.CalculatorApp;

import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom Slider. This is a progress bar.
 */
public class Slider extends LinearLayout
{
    private List<SliderObserver> observers;
    private SeekBar seekBar;

    /**
     * Constructor.
     *
     * @param ctx
     */
    public Slider(Context ctx)
    {
        super(ctx);

        this.observers = new ArrayList<SliderObserver>();

        CalculatorApp app = (CalculatorApp) ctx.getApplicationContext();
        CTheme t = app.getCTheme();

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        // minus button to decrement progress
        IconButton minusBtn = new IconButton(ctx, R.drawable.minus);
        minusBtn.setOnClickListener(v -> setProgress(seekBar.getProgress() - 1));
        minusBtn.setIconColor(t.getAccentColor());
        addView(minusBtn);

        // the actual slider
        seekBar = new SeekBar(ctx);
        seekBar.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
        seekBar.setMax(100);
        seekBar.setProgress(50);
        seekBar.setBackgroundColor(t.getBackgroundSecColor());
        seekBar.getProgressDrawable().setColorFilter(t.getBackgroundSecColor(), android.graphics.PorterDuff.Mode.MULTIPLY);
        seekBar.getProgressDrawable().setColorFilter(t.getAccentColor(), android.graphics.PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(t.getAccentColor(), android.graphics.PorterDuff.Mode.SRC_IN);
        addView(seekBar);

        // plus button to increment progress
        IconButton plusBtn = new IconButton(ctx, R.drawable.plus);
        plusBtn.setOnClickListener(v -> setProgress(seekBar.getProgress() + 1));
        plusBtn.setIconColor(t.getAccentColor());
        addView(plusBtn);

        // the listener for dragging
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar sb, int progress, boolean fromUser) { alertObservers(progress); }

            @Override
            public void onStartTrackingTouch(SeekBar sb) {}

            @Override
            public void onStopTrackingTouch(SeekBar sb) {}
        });
    }

    /**
     * Sets the progress of the slider directly. Alerts the observers,
     *
     * @param value The value to set to
     */
    public void setProgress(int value)
    {
        if (value < 0) value = 0;
        if (value > seekBar.getMax()) value = seekBar.getMax();
        seekBar.setProgress(value);
        alertObservers(value);
    }

    /**
     * Getter for the progress.
     * @return The current progress
     */
    public int getProgress() {
        return seekBar.getProgress();
    }

    /**
     * Sets new bounds for the progress.
     * @param max The new upper bound
     * @param reset True if progress should go to center and reset
     */
    public void setBounds(int max, boolean reset)
    {
        seekBar.setMax(max);
        if (reset) seekBar.setProgress(max / 2);
    }

    /**
     * Notifies all observers that a change has happened
     * @param newValue
     */
    public void alertObservers(int newValue)
    {
        for (SliderObserver obs : observers)
        {
            obs.handleSliderChange(newValue);
        }
    }

    /**
     * Removes an observer
     *
     * @param obs The observer to remove
     */
    public void removeObserver(SliderObserver obs)
    {
        observers.remove(obs);
    }

    /**
     * Adds an observer
     *
     * @param obs The observer to add
     */
    public void addObserver(SliderObserver obs)
    {
        if (obs != null && !observers.contains(obs))
        {
            observers.add(obs);
        }
    }
}
