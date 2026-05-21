package com.hbtipcalc.tipcalculator.models;

public enum TipInputType {
    SLIDER(0),
    QUICK_BUTTONS(1);

    private final int value;

    TipInputType(int value) { this.value = value; }

    public int getValue() { return value; }

    public static TipInputType fromInt(int i) {
        for (TipInputType t : values()) {
            if (t.value == i) return t;
        }
        return SLIDER;
    }
}
