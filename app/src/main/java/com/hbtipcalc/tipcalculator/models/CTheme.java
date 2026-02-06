package com.hbtipcalc.tipcalculator.models;

import android.graphics.Color;
import android.graphics.Typeface;

/**
 * Enum to represent the available themes.
 */
public enum CTheme
{
    GRUVBOX(
            Color.parseColor("#ebdbb2"),
            Color.parseColor("#282828"),
            Color.parseColor("#282828"),
            Color.parseColor("#504945"),
            Color.parseColor("#fabd2f"),
            0
    ),

    DRACULA(
            Color.parseColor("#f8f8f2"),
            Color.parseColor("#282a36"),
            Color.parseColor("#282a36"),
            Color.parseColor("#44475a"),
            Color.parseColor("#bd93f9"),
            1
    ),

    MOCHA(
            Color.parseColor("#5a5148"),
            Color.parseColor("#e9d4b8"),
            Color.parseColor("#f3e9de"),
            Color.parseColor("#e4d7c7"),
            Color.parseColor("#c47a3e"),
            2
    ),
    ;

    // color vars
    private final int text;
    private final int header;
    private final int background;
    private final int backgroundSec;
    private final int accent;
    private final int id;

    // font styling
    private static final Typeface FONT = Typeface.MONOSPACE;
    private static final float NUM_PAD_BTN_FS = 26f;
    private static final float TITLE_FS = 24f;
    private static final float TEXT_FS = 18f;

    /**
     * Constructor
     *
     * @param text The text color
     * @param header The header color
     * @param background The background color
     * @param backgroundSec The background secondary color
     * @param accent The accent color
     */
    CTheme(int text, int header, int background, int backgroundSec, int accent, int id)
    {
        this.text = text;
        this.header = header;
        this.background = background;
        this.backgroundSec = backgroundSec;
        this.accent = accent;
        this.id = id;
    }

    // getter
    public int getID() { return this.id; }

    // getter methods for colors
    public int getTextColor() { return this.text; }
    public int getHeaderColor() { return this.header; }
    public int getBackgroundColor() { return this.background; }
    public int getBackgroundSecColor() { return this.backgroundSec; }
    public int getAccentColor() { return this.accent; }

    // getter methods for fonts
    public Typeface getFont() { return FONT; }
    public float getTileFontSize() { return TITLE_FS; }
    public float getTextFontSize() { return TEXT_FS; }
    public float getNumPadBtnFontSize() { return NUM_PAD_BTN_FS; }
}
