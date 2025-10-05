package com.hbtipcalc.tipcalculator.models;
import android.app.Application;

import com.hbtipcalc.tipcalculator.controllers.Calculator;

import java.math.BigDecimal;

/**
 * Application which stores the global App Data.
 */
public class CalculatorApp extends Application
{
    private Settings settings;
    private CTheme ctheme;
    private Calculator calculator;

    public CalculatorApp()
    {
        super.onCreate();
        this.ctheme = CTheme.ATOM_ONE_DARKER; // THEMES: Gruvbox, Solarized_Dark, Dracula, Nord, Monokai, Earth
        this.settings = loadSettings();
        this.calculator = createCalculator();
    }

    public Settings loadSettings()
    {
        // TODO: Load the settings object and set instance var
        return null;
    }

    public Calculator createCalculator()
    {
        // TODO: the rounding flag and tip percent (default) set here should be set from settings
        return new Calculator(new BigDecimal("100.00"), 25, RoundingFlag.NONE);
    }

    // Getter methods
    public Settings getSettings() { return this.settings; };
    public CTheme getCTheme() { return this.ctheme; };
    public Calculator getCalculator() { return this.calculator; }

    // Setter methods
    public void setCTheme(CTheme theme) { this.ctheme = theme; }
}
