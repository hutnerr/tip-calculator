package com.hbtipcalc.tipcalculator.models;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Holds all UI sizing constants (font sizes, paddings, margins) for a given
 * screen size tier. One instance is created at app startup via {@link #forDevice(Context)}
 * and stored in {@link CalculatorApp}. All view components read from it instead of
 * hardcoding values.
 *
 * Font sizes are in sp (pass directly to TextView.setTextSize()).
 * All other dimensions are pre-computed in px (pass directly to setPadding / LayoutParams).
 */
public class ScreenProfile {

    // ── Font sizes (sp) ─────────────────────────────────────────────────────
    private final float numPadBtnFontSize;
    private final float titleFontSize;
    private final float textFontSize;

    // ── Element container (the card-like section wrappers) ───────────────────
    private final int containerHPadding;    // outer horizontal padding
    private final int containerVPadding;    // outer vertical padding
    private final int contentPadding;       // inner rounded-box padding

    // ── Result rows (Tip / Total / Split lines) ──────────────────────────────
    private final int rowHPadding;
    private final int rowVPaddingTop;
    private final int rowVPaddingBottom;

    // ── NumPad ────────────────────────────────────────────────────────────────
    private final int numPadPadding;        // outer grid padding
    private final int numPadBtnVPadding;    // per-button vertical padding

    // ── Icon buttons (header) ────────────────────────────────────────────────
    private final int iconBtnSize;          // width and height

    // ── Bill amount text box ─────────────────────────────────────────────────
    private final int textBoxHPadding;
    private final int textBoxVPadding;

    // ── Dropdown items ───────────────────────────────────────────────────────
    private final int dropdownItemPadding;

    // ─────────────────────────────────────────────────────────────────────────

    private ScreenProfile(
            float numPadBtnFontSize, float titleFontSize, float textFontSize,
            int containerHPadding, int containerVPadding, int contentPadding,
            int rowHPadding, int rowVPaddingTop, int rowVPaddingBottom,
            int numPadPadding, int numPadBtnVPadding,
            int iconBtnSize,
            int textBoxHPadding, int textBoxVPadding,
            int dropdownItemPadding) {
        this.numPadBtnFontSize   = numPadBtnFontSize;
        this.titleFontSize       = titleFontSize;
        this.textFontSize        = textFontSize;
        this.containerHPadding   = containerHPadding;
        this.containerVPadding   = containerVPadding;
        this.contentPadding      = contentPadding;
        this.rowHPadding         = rowHPadding;
        this.rowVPaddingTop      = rowVPaddingTop;
        this.rowVPaddingBottom   = rowVPaddingBottom;
        this.numPadPadding       = numPadPadding;
        this.numPadBtnVPadding   = numPadBtnVPadding;
        this.iconBtnSize         = iconBtnSize;
        this.textBoxHPadding     = textBoxHPadding;
        this.textBoxVPadding     = textBoxVPadding;
        this.dropdownItemPadding = dropdownItemPadding;
    }

    // ── Public factory ────────────────────────────────────────────────────────

    /**
     * Selects and constructs the appropriate profile for the device's screen size.
     * Should be called once at application startup.
     */
    public static ScreenProfile forDevice(Context ctx) {
        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        float d  = dm.density;
        int   sw = ctx.getResources().getConfiguration().smallestScreenWidthDp;

        if (sw < 360) return small(d);
        if (sw < 600) return medium(d);
        if (sw < 720) return large(d);
        return tablet(d);
    }

    // ── Tier definitions ──────────────────────────────────────────────────────

    /** Small — sw < 360dp (minority of older/budget phones) */
    private static ScreenProfile small(float d) {
        return new ScreenProfile(
                14f, 17f, 13f,
                px(d, 12), px(d, 3),  px(d, 6),
                px(d, 12), px(d, 5),  px(d, 2),
                px(d, 5),  px(d, 5),
                px(d, 44),
                px(d, 4),  px(d, 2),
                px(d, 5)
        );
    }

    /** Medium — sw 360–599dp (typical modern phones, e.g. 360dp and 411dp) */
    private static ScreenProfile medium(float d) {
        return new ScreenProfile(
                20f, 21f, 16f,
                px(d, 19), px(d, 6),  px(d, 10),
                px(d, 19), px(d, 8),  px(d, 4),
                px(d, 8),  px(d, 10),
                px(d, 54),
                px(d, 8),  px(d, 4),
                px(d, 7)
        );
    }

    /** Large — sw 600–719dp (large phones, phablets, foldables) */
    private static ScreenProfile large(float d) {
        return new ScreenProfile(
                24f, 23f, 18f,
                px(d, 22), px(d, 7),  px(d, 11),
                px(d, 22), px(d, 9),  px(d, 5),
                px(d, 9),  px(d, 12),
                px(d, 62),
                px(d, 14), px(d, 8),
                px(d, 18)
        );
    }

    /** Tablet — sw ≥ 720dp (e.g. 2560×1600 ≈ 800dp sw) */
    private static ScreenProfile tablet(float d) {
        return new ScreenProfile(
                32f, 28f, 22f,
                px(d, 28), px(d, 10), px(d, 14),
                px(d, 28), px(d, 12), px(d, 8),
                px(d, 12), px(d, 18),
                px(d, 76),
                px(d, 20), px(d, 12),
                px(d, 28)
        );
    }

    private static int px(float density, int dp) {
        return Math.round(dp * density);
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public float getNumPadBtnFontSize()  { return numPadBtnFontSize; }
    public float getTitleFontSize()      { return titleFontSize; }
    public float getTextFontSize()       { return textFontSize; }

    public int getContainerHPadding()    { return containerHPadding; }
    public int getContainerVPadding()    { return containerVPadding; }
    public int getContentPadding()       { return contentPadding; }

    public int getRowHPadding()          { return rowHPadding; }
    public int getRowVPaddingTop()       { return rowVPaddingTop; }
    public int getRowVPaddingBottom()    { return rowVPaddingBottom; }

    public int getNumPadPadding()        { return numPadPadding; }
    public int getNumPadBtnVPadding()    { return numPadBtnVPadding; }

    public int getIconBtnSize()          { return iconBtnSize; }

    public int getTextBoxHPadding()      { return textBoxHPadding; }
    public int getTextBoxVPadding()      { return textBoxVPadding; }

    public int getDropdownItemPadding()  { return dropdownItemPadding; }
}
