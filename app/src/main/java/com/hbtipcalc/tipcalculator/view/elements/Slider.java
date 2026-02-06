package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import com.hbtipcalc.tipcalculator.R;
import com.hbtipcalc.tipcalculator.models.CTheme;
import com.hbtipcalc.tipcalculator.models.CalculatorApp;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom slider component with increment/decrement buttons.
 * Features a seekbar with plus/minus buttons for precise value adjustment
 * and observer pattern for value change notifications.
 */
public class Slider extends LinearLayout
{
    private final List<SliderObserver> observers;
    private final SeekBar seekBar;
    private String sliderID;
    private int min = 0;
    private int max = 100;

    /**
     * Constructs a new Slider component.
     *
     * @param ctx The application context
     * @param sid The unique identifier for this slider
     */
    public Slider(Context ctx, String sid)
    {
        super(ctx);

        this.sliderID = sid;
        this.observers = new ArrayList<>();

        CalculatorApp app = (CalculatorApp) ctx.getApplicationContext();
        CTheme t = app.getCTheme();

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        int verticalPadding = dpToPx(ctx, -8);
        setPadding(0, verticalPadding, 0, verticalPadding);

        // minus button
        IconButton minusBtn = new IconButton(ctx, R.drawable.minus);
        minusBtn.setOnClickListener(v -> setProgress(getProgress() - 1));
        minusBtn.setIconColor(t.getAccentColor());
        addView(minusBtn);

        // seekbar
        seekBar = new SeekBar(ctx);
        seekBar.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
        seekBar.setMax(max - min);
        seekBar.setProgress(0);
        seekBar.setBackgroundColor(t.getBackgroundSecColor());
        seekBar.getProgressDrawable().setColorFilter(t.getAccentColor(), android.graphics.PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(t.getAccentColor(), android.graphics.PorterDuff.Mode.SRC_IN);
        addView(seekBar);

        // plus button
        IconButton plusBtn = new IconButton(ctx, R.drawable.plus);
        plusBtn.setOnClickListener(v -> setProgress(getProgress() + 1));
        plusBtn.setIconColor(t.getAccentColor());
        addView(plusBtn);

        // listener
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar sb, int progress, boolean fromUser)
            {
                notifyObservers(progress + min);
            }

            @Override
            public void onStartTrackingTouch(SeekBar sb) {}

            @Override
            public void onStopTrackingTouch(SeekBar sb) {}
        });
    }

    /**
     * Sets the slider's current value.
     * The value is automatically clamped to the min/max bounds.
     *
     * @param value The desired slider value
     */
    public void setProgress(int value)
    {
        int clamped = Math.max(min, Math.min(value, max));
        seekBar.setProgress(clamped - min);
        notifyObservers(clamped);
    }

    /**
     * Gets the slider's current value.
     *
     * @return The current slider value (includes minimum offset)
     */
    public int getProgress()
    {
        return seekBar.getProgress() + min;
    }

    /**
     * Sets the unique identifier for this slider.
     *
     * @param id The slider identifier
     */
    public void setID(String id)
    {
        this.sliderID = id;
    }

    /**
     * Gets the unique identifier for this slider.
     *
     * @return The slider identifier
     */
    public String getID()
    {
        return this.sliderID;
    }

    /**
     * Sets both the minimum and maximum bounds for the slider.
     *
     * @param min The minimum value
     * @param max The maximum value
     * @param reset Whether to reset the slider to the midpoint of the new range
     */
    public void setBounds(int min, int max, boolean reset)
    {
        setMinimum(min);
        setMax(max, reset);
    }

    /**
     * Sets the minimum value for the slider.
     * Adjusts the current progress if it falls outside the new bounds.
     *
     * @param min The minimum value
     */
    public void setMinimum(int min)
    {
        this.min = min;
        seekBar.setMax(max - min);
        int p = getProgress();
        if (p < min) setProgress(min);
        if (p > max) setProgress(max);
    }

    /**
     * Sets the maximum value for the slider.
     * Optionally resets the slider to the midpoint of the range.
     *
     * @param max The maximum value
     * @param reset Whether to reset the slider to the midpoint
     */
    public void setMax(int max, boolean reset)
    {
        this.max = max;
        seekBar.setMax(max - min);
        if (reset) setProgress((min + max) / 2);
    }

    /**
     * Notifies all registered observers of a value change.
     *
     * @param value The new slider value
     */
    public void notifyObservers(int value)
    {
        for (SliderObserver observer : observers)
        {
            observer.handleSliderChange(value, sliderID);
        }
    }

    /**
     * Registers an observer to be notified of slider value changes.
     * Prevents duplicate registrations.
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

    /**
     * Unregisters an observer from slider value change notifications.
     *
     * @param obs The observer to remove
     */
    public void removeObserver(SliderObserver obs)
    {
        observers.remove(obs);
    }

    /**
     * Converts density-independent pixels (dp) to actual pixels (px).
     *
     * @param ctx The application context
     * @param dp The value in dp to convert
     * @return The equivalent value in pixels
     */
    private int dpToPx(Context ctx, int dp)
    {
        float density = ctx.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}