package com.hbtipcalc.tipcalculator.view.elements;

/**
 * Interface for things that should update when a slider changes.
 */
public interface SliderObserver
{
    /**
     * Implemented method must do something with the new value of a slider.
     * 
     * @param newProgress The new progress of the slider
     */
    void handleSliderChange(int newProgress);
}
