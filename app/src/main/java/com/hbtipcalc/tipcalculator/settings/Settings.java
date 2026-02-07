package com.hbtipcalc.tipcalculator.settings;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.rxjava3.RxDataStore;

import com.hbtipcalc.tipcalculator.math.TipCalculator;
import com.hbtipcalc.tipcalculator.models.CTheme;
import com.hbtipcalc.tipcalculator.models.RoundingFlag;
import com.hbtipcalc.tipcalculator.models.TipResult;
import com.hbtipcalc.tipcalculator.util.Clogger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.core.Single;

public class Settings
{
    private static final int DEFAULT_TIP_PERCENT = 15; // 15%
    private static final RoundingFlag DEFAULT_ROUND_FLAG = RoundingFlag.NONE; // no rounding
    private static final String DEFAULT_CURRENCY = "$"; // USD
    private static final CTheme DEFAULT_THEME = CTheme.GRUVBOX; // Gruvbox
    private static final boolean DEFAULT_SPLIT_ACTIVE = false; // split must be toggled first

    private int tipPercentage;
    private RoundingFlag roundFlag;
    private String currency;
    private CTheme theme;
    private boolean splitActive;

    private static Settings instance; // singleton
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    private Settings() { }

    public static Settings getInstance()
    {
        if (instance == null) instance = new Settings();
        return instance;
    }

    // load on startup
    public void loadSettings()
    {
        try
        {
            executor.submit(() -> {
                RxDataStore<Preferences> dataStore = SettingsDataStore.getDataStore();
                Preferences prefs = dataStore.data().firstOrError().blockingGet();

                // load values, use defaults if inaccessible
                Integer tip = prefs.get(SettingsKeys.TIP_PERCENTAGE);
                this.tipPercentage = tip != null ? tip : DEFAULT_TIP_PERCENT;

                Integer flagValue = prefs.get(SettingsKeys.ROUND_FLAG);
                this.roundFlag = flagValue != null ? RoundingFlag.fromInt(flagValue) : DEFAULT_ROUND_FLAG;

                String curr = prefs.get(SettingsKeys.CURRENCY);
                this.currency = curr != null ? curr : DEFAULT_CURRENCY;
                TipResult.setCurrencySymbol(this.currency);

                Integer themeId = prefs.get(SettingsKeys.THEME);
                this.theme = themeId != null ? CTheme.values()[themeId] : DEFAULT_THEME;

                Integer intSplitActive = prefs.get(SettingsKeys.SPLIT_ACTIVE);
                if (intSplitActive == null) this.splitActive = DEFAULT_SPLIT_ACTIVE;
                else if (intSplitActive == 0) this.splitActive = false;
                else if (intSplitActive == 1) this.splitActive = true;
                else
                {
                    Clogger.warn("Split active was an int not 0 or 1");
                    this.splitActive = DEFAULT_SPLIT_ACTIVE;
                }

            }).get(); // wait for completion
            Clogger.info("Settings successfully loaded");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.tipPercentage = DEFAULT_TIP_PERCENT;
            this.roundFlag = DEFAULT_ROUND_FLAG;
            this.currency = DEFAULT_CURRENCY;
            this.splitActive = DEFAULT_SPLIT_ACTIVE;
        }
    }

    public void reset()
    {
        setTipPercentage(DEFAULT_TIP_PERCENT);
        setRoundFlag(DEFAULT_ROUND_FLAG);
        setCurrency(DEFAULT_CURRENCY);
        setTheme(DEFAULT_THEME);
        setSplitActive(DEFAULT_SPLIT_ACTIVE);
        Clogger.info("Settings have been reset");
    }

    // GETTER METHODS
    public int getTipPercentage() { return tipPercentage; }
    public RoundingFlag getRoundFlag() { return roundFlag; }
    public String getCurrency() { return currency; }
    public CTheme getTheme() { return theme; }
    public boolean isSplitActive() { return splitActive; }

    // SETTER METHODS
    public void setTipPercentage(int percentage)
    {
        this.tipPercentage = percentage;
        saveValue(SettingsKeys.TIP_PERCENTAGE, percentage);
    }

    public void setRoundFlag(RoundingFlag flag)
    {
        this.roundFlag = flag;
        saveValue(SettingsKeys.ROUND_FLAG, flag.getValue());
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
        TipResult.setCurrencySymbol(currency);
        saveValue(SettingsKeys.CURRENCY, currency);
    }

    public void setTheme(CTheme theme)
    {
        this.theme = theme;
        saveValue(SettingsKeys.THEME, theme.getID());
    }

    public void setSplitActive(boolean status)
    {
        this.splitActive = status;
        int i = status ? 1 : 0; // convert to int
        saveValue(SettingsKeys.SPLIT_ACTIVE, i);
    }

    private <T> void saveValue(Preferences.Key<T> key, T value)
    {
        executor.execute(() -> {
            RxDataStore<Preferences> dataStore = SettingsDataStore.getDataStore();
            dataStore.updateDataAsync(prefs -> {
                MutablePreferences mutablePrefs = prefs.toMutablePreferences();
                mutablePrefs.set(key, value);
                return Single.just(mutablePrefs);
            }).blockingSubscribe();
        });
        Clogger.debug("A setting has been updated");
    }
}