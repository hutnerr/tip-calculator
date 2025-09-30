package com.hbtipcalc.tipcalculator.view.elements;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import com.hbtipcalc.tipcalculator.R;
import com.hbtipcalc.tipcalculator.styles.StyleConstants;

import android.widget.SeekBar;

import java.util.List;

public class Slider extends LinearLayout {
    private List<SliderObserver> observers;

    private SeekBar seekBar;
    private OnSliderChangeListener listener;

    public interface OnSliderChangeListener
    {
        void onProgressChanged(int progress);
    }

    public void setOnSliderChangeListener(OnSliderChangeListener l)
    {
        this.listener = l;
    }

    public Slider(Context ctx) {
        super(ctx);

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        // minus button to decrement progress
        IconButton minusBtn = new IconButton(ctx, R.drawable.minus);
        minusBtn.setOnClickListener(v -> setProgress(seekBar.getProgress() - 1));
        minusBtn.setIconColor(StyleConstants.COLOR_ACCENT);
        addView(minusBtn);

        // the actual slider
        seekBar = new SeekBar(ctx);
        seekBar.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
        seekBar.setMax(100);
        seekBar.setProgress(50);
        seekBar.setBackgroundColor(StyleConstants.COLOR_BACKGROUND_TWO);
        seekBar.getProgressDrawable().setColorFilter(StyleConstants.COLOR_BACKGROUND_TWO, android.graphics.PorterDuff.Mode.MULTIPLY);
        seekBar.getProgressDrawable().setColorFilter(StyleConstants.COLOR_ACCENT, android.graphics.PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(StyleConstants.COLOR_ACCENT, android.graphics.PorterDuff.Mode.SRC_IN);
        addView(seekBar);

        // plus button to increment progress
        IconButton plusBtn = new IconButton(ctx, R.drawable.plus);
        plusBtn.setOnClickListener(v -> setProgress(seekBar.getProgress() + 1));
        plusBtn.setIconColor(StyleConstants.COLOR_ACCENT);
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

    public void setProgress(int value)
    {
        if (value < 0) value = 0;
        if (value > seekBar.getMax()) value = seekBar.getMax();
        seekBar.setProgress(value);
        alertObservers(value);
    }

    public int getProgress() {
        return seekBar.getProgress();
    }

    public void setBounds(int max, boolean reset)
    {
        seekBar.setMax(max);
        if (reset) seekBar.setProgress(max / 2);
    }

    public void setColor(int color)
    {
        seekBar.getProgressDrawable().setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public void alertObservers(int newValue)
    {
        for (SliderObserver obs : observers)
        {
            obs.handleSliderChange(newValue);
        }
    }

    public void removeObserver(SliderObserver obs)
    {
        observers.remove(obs);
    }

    public void addObserver(SliderObserver obs)
    {
        if (obs != null && !observers.contains(obs))
        {
            observers.add(obs);
        }
    }


}
