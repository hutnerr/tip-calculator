package com.hbtipcalc.tipcalculator.view.elements;

/**
 * Observer interface for receiving text change notifications from TextBox components.
 * Implement this interface to be notified when a TextBox's formatted text value changes.
 */
public interface TextBoxObserver
{
    /**
     * Called when the TextBox's text value changes.
     *
     * @param newText The new formatted text value
     */
    void handleText(String newText);
}