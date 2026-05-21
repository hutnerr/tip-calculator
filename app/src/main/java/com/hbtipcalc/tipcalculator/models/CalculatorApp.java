package com.hbtipcalc.tipcalculator.models;
import android.app.Application;

import com.hbtipcalc.tipcalculator.controllers.Calculator;
import com.hbtipcalc.tipcalculator.settings.Settings;
import com.hbtipcalc.tipcalculator.settings.SettingsDataStore;
import com.hbtipcalc.tipcalculator.util.Clogger;

import java.math.BigDecimal;

/**
 * Application which stores the global App Data.
 */
public class CalculatorApp extends Application
{
    private Settings settings;
    private CTheme ctheme;
    private Calculator calculator;
    private ScreenProfile screenProfile;

    @Override
    public void onCreate()
    {
        super.onCreate();

        SettingsDataStore.init(this);
        Settings.getInstance().loadSettings();
        this.settings = Settings.getInstance();

        this.ctheme = settings.getTheme();
        this.calculator = new Calculator(new BigDecimal("0.00"), settings.getTipPercentage(), settings.getRoundFlag());
        this.screenProfile = ScreenProfile.forDevice(this);

        Clogger.info("CalculatorApp Initialized");
    }

    // getter methods
    public Settings getSettings()         { return this.settings; }
    public CTheme getCTheme()             { return this.ctheme; }
    public Calculator getCalculator()     { return this.calculator; }
    public ScreenProfile getScreenProfile() { return this.screenProfile; }

    // setter methods
    public void setCTheme(CTheme t)
    {
        this.ctheme = t;

    }
}