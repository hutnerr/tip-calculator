package com.hbtipcalc.tipcalculator.models;

/**
 * UP = Always round up
 * DOWN = Always round down
 * DYNAMIC = Round up/down based on amt eg 51c up, 50down, etc.
 * NONE = Gives back the exact value
 */
public enum RoundingFlag
{
    UP, DOWN, DYNAMIC, NONE
}
