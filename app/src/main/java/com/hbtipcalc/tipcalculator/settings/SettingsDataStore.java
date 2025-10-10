package com.hbtipcalc.tipcalculator.settings;

import android.content.Context;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

public class SettingsDataStore
{
    private static RxDataStore<Preferences> dataStore;

    public static void init(Context context)
    {
        if (dataStore != null) return;
        dataStore = new RxPreferenceDataStoreBuilder(context, "settings").build();
    }

    public static RxDataStore<Preferences> getDataStore()
    {
        if (dataStore == null)
        {
            throw new IllegalStateException("DataStore not initialized. Call init() first.");
        }
        return dataStore;
    }
}