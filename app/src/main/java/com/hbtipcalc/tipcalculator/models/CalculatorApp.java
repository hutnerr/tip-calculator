package com.hbtipcalc.tipcalculator.models;
import android.app.Application;

import com.hbtipcalc.tipcalculator.controllers.Calculator;
import com.hbtipcalc.tipcalculator.settings.Settings;
import com.hbtipcalc.tipcalculator.settings.SettingsDataStore;

import java.math.BigDecimal;

/**
 * Application which stores the global App Data.
 */
public class CalculatorApp extends Application
{
    private Settings settings;
    private CTheme ctheme;
    private Calculator calculator;

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Initialize settings
        SettingsDataStore.init(this);
        Settings.getInstance().loadSettings();
        this.settings = Settings.getInstance();

        // TODO: the theme needs to be loaded from settings.
        this.ctheme = CTheme.GRUVBOX; // THEMES: Gruvbox, Solarized_Dark, Dracula, Nord, Monokai, Earth

        // Create calculator with loaded settings
        this.calculator = createCalculator();
    }

    /**
     * Helper which creates the calculator controller based on the settings.
     *
     * @return The calculator
     */
    private Calculator createCalculator()
    {
        return new Calculator(new BigDecimal("0.00"), settings.getTipPercentage(), settings.getRoundFlag());
    }

    // Getter methods
    public Settings getSettings() { return this.settings; }
    public CTheme getCTheme() { return this.ctheme; }
    public Calculator getCalculator() { return this.calculator; }

    // Setter methods
    public void setCTheme(CTheme theme) { this.ctheme = theme; }
}