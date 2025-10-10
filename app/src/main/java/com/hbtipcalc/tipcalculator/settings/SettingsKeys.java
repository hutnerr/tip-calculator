package com.hbtipcalc.tipcalculator.settings;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;

import com.hbtipcalc.tipcalculator.models.RoundingFlag;

public class SettingsKeys
{
    public static final Preferences.Key<Integer> TIP_PERCENTAGE = PreferencesKeys.intKey("tip_percentage");
    public static final Preferences.Key<Integer> ROUND_FLAG = PreferencesKeys.intKey("round_flag");
    public static final Preferences.Key<String> CURRENCY = PreferencesKeys.stringKey("currency");
}