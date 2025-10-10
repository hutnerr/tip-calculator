package com.hbtipcalc.tipcalculator.models;

/**
 * UP = Always round up
 * DOWN = Always round down
 * DYNAMIC = Round up/down based on amt eg 51c up, 50down, etc.
 * NONE = Gives back the exact value
 */
public enum RoundingFlag
{
    UP(0),         // always round tip up (6.03 -> 7.00)
    DOWN(1),       // always round tip down (6.65 -> 6.00)
    DYNAMIC(2),    // round based on closest (5.51 -> 6.00 and 5.50 -> 5.00)
    NONE(3);       // no rounding performed

    private final int value;

    RoundingFlag(int value)
    {
        this.value = value;
    }

    public static RoundingFlag fromInt(int value)
    {
        for (RoundingFlag flag : values())
        {
            if (flag.value == value) return flag;
        }
        return NONE; // default if invalid value
    }

    public int getValue() { return value; }
}