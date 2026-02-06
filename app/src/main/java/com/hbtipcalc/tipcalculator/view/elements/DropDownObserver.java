package com.hbtipcalc.tipcalculator.view.elements;

/**
 * Interface to allow things to receive updates from a dropdown selection.
 */
public interface DropDownObserver
{
    /**
     * Handle a change from a dropdown selection.
     *
     * @param position The position of the item in the dropdown
     * @param value The value of the item
     */
    void handleDropDownChange(int position, String value);
}
