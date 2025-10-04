package com.hbtipcalc.tipcalculator.models;
import android.app.Application;

import com.hbtipcalc.tipcalculator.controllers.Calculator;

import java.math.BigDecimal;

public class CalculatorApp extends Application
{
    private Settings settings;
    private CTheme ctheme;
    private Calculator calculator;

    public CalculatorApp()
    {
        super.onCreate();
        this.ctheme = CTheme.DRACULA; // THEMES: Gruvbox, Solarized_Dark, Dracula, Nord, Monokai, Earth
        this.calculator = new Calculator(new BigDecimal("100.00"), 25, RoundingFlag.NONE);
    }

    public Settings getSettings() { return this.settings; };
    public CTheme getCTheme() { return this.ctheme; };
    public Calculator getCalculator() { return this.calculator; }

    public void setCTheme(CTheme theme) { this.ctheme = theme; }
}
