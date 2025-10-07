package com.hbtipcalc.tipcalculator.models;

/**
 * UP = Always round up
 * DOWN = Always round down
 * DYNAMIC = Round up/down based on amt eg 51c up, 50down, etc.
 * NONE = Gives back the exact value
 */
public enum RoundingFlag
{
    UP,         // always round tip up (6.03 -> 7.00)
    DOWN,       // always round tip down (6.65 -> 6.00)
    DYNAMIC,    // round based on closest (5.51 -> 6.00 and 5.50 -> 5.00)
    NONE        // no rounding performed
}

