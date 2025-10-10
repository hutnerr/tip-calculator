package com.hbtipcalc.tipcalculator.settings;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.rxjava3.RxDataStore;

import com.hbtipcalc.tipcalculator.models.RoundingFlag;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.core.Single;

public class Settings
{
    private static final int DEFAULT_TIP_PERCENT = 15;
    private static final RoundingFlag DEFAULT_ROUND_FLAG = RoundingFlag.NONE;
    private static final String DEFAULT_CURRENCY = "$";

    private int tipPercentage;
    private RoundingFlag roundFlag;
    private String currency;

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
            }).get(); // wait for completion
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.tipPercentage = DEFAULT_TIP_PERCENT;
            this.roundFlag = DEFAULT_ROUND_FLAG;
            this.currency = DEFAULT_CURRENCY;
        }
    }

    public int getTipPercentage() { return tipPercentage; }
    public RoundingFlag getRoundFlag() { return roundFlag; }
    public String getCurrency() { return currency; }

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
        saveValue(SettingsKeys.CURRENCY, currency);
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
    }
}