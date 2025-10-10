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

public class Slider extends LinearLayout
{
    private final List<SliderObserver> observers;
    private final SeekBar seekBar;
    private int min = 0;
    private int max = 100;

    public Slider(Context ctx)
    {
        super(ctx);

        this.observers = new ArrayList<>();

        CalculatorApp app = (CalculatorApp) ctx.getApplicationContext();
        CTheme t = app.getCTheme();

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

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
            @Override public void onStartTrackingTouch(SeekBar sb) {}
            @Override public void onStopTrackingTouch(SeekBar sb) {}
        });
    }

    public void setProgress(int value)
    {
        int clamped = Math.max(min, Math.min(value, max));
        seekBar.setProgress(clamped - min);
        notifyObservers(clamped);
    }

    public int getProgress()
    {
        return seekBar.getProgress() + min;
    }

    public void setBounds(int min, int max, boolean reset)
    {
        setMinium(min);
        setMax(max, reset);
    }

    public void setMinium(int min)
    {
        this.min = min;
        seekBar.setMax(max - min);
        int p = getProgress();
        if (p < min) setProgress(min);
        if (p > max) setProgress(max);
    }

    public void setMax(int max, boolean reset)
    {
        this.max = max;
        seekBar.setMax(max - min);
        if (reset) setProgress((min + max) / 2);
    }

    public void notifyObservers(int value)
    {
        for (SliderObserver observer : observers)
        {
            observer.handleSliderChange(value);
        }
    }

    public void addObserver(SliderObserver obs)
    {
        if (obs != null && !observers.contains(obs)) observers.add(obs);
    }

    public void removeObserver(SliderObserver obs)
    {
        observers.remove(obs);
    }
}
