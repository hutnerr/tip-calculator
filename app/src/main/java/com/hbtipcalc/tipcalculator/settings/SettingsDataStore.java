package com.hbtipcalc.tipcalculator.settings;

import android.content.Context;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import com.hbtipcalc.tipcalculator.util.Clogger;

public class SettingsDataStore
{
    private static RxDataStore<Preferences> dataStore;

    public static void init(Context context)
    {
        if (dataStore != null)
        {
            Clogger.warn("Trying to initialize the data store again...");
            return;
        }
        dataStore = new RxPreferenceDataStoreBuilder(context, "settings").build();
    }

    public static RxDataStore<Preferences> getDataStore()
    {
        if (dataStore == null)
        {
            Clogger.error("DataStore not initialized. Call init() first.");
            throw new IllegalStateException("DataStore not initialized. Call init() first.");
        }
        return dataStore;
    }
}