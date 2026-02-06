package com.hbtipcalc.tipcalculator.settings;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;

/**
 * These are the keys for the settings. See more in Settings.java
 */
public class SettingsKeys
{
    public static final Preferences.Key<Integer> TIP_PERCENTAGE = PreferencesKeys.intKey("tip_percentage"); // the default tip percentage
    public static final Preferences.Key<Integer> ROUND_FLAG = PreferencesKeys.intKey("round_flag"); // the int value of the enum rounding flag
    public static final Preferences.Key<String> CURRENCY = PreferencesKeys.stringKey("currency"); // the currency symbol
    public static final Preferences.Key<Integer> THEME = PreferencesKeys.intKey("theme"); // the CTheme
    public static final Preferences.Key<Integer> SPLIT_ACTIVE = PreferencesKeys.intKey("split_active"); // 0 for inactive, 1 for active
}