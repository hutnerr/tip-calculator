package com.hbtipcalc.tipcalculator.models;
import android.app.Application;

public class CalculatorApp extends Application
{
    private Settings settings;
    private CTheme theme;

    public CalculatorApp()
    {
        super.onCreate();

        // TODO: initalize the app wide settings and stuff here
    }

    public Settings getSettings() { return this.settings; };
    public CTheme getCTheme() { return this.theme; };
    public void setCTheme(CTheme theme) { this.theme = theme; }
}
