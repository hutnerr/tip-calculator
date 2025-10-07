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
            Color.parseColor("#fabd2f")
    ),

    SOLARIZED_DARK(
            Color.parseColor("#839496"),
            Color.parseColor("#002b36"),
            Color.parseColor("#002b36"),
            Color.parseColor("#073642"),
            Color.parseColor("#b58900")
    ),

    DRACULA(
            Color.parseColor("#f8f8f2"),
            Color.parseColor("#282a36"),
            Color.parseColor("#282a36"),
            Color.parseColor("#44475a"),
            Color.parseColor("#bd93f9")
    ),

    NORD(
            Color.parseColor("#d8dee9"),
            Color.parseColor("#2e3440"),
            Color.parseColor("#2e3440"),
            Color.parseColor("#3b4252"),
            Color.parseColor("#88c0d0")
    ),

    MONOKAI(
            Color.parseColor("#f8f8f2"),
            Color.parseColor("#272822"),
            Color.parseColor("#272822"),
            Color.parseColor("#3e3d32"),
            Color.parseColor("#f92672")
    ),

    EARTH(
            Color.parseColor("#e0e2db"),
            Color.parseColor("#2e3d2f"),
            Color.parseColor("#1f2d20"),
            Color.parseColor("#3a4b3c"),
            Color.parseColor("#8f9779")
    ),

    ATOM_ONE_DARKER(
        Color.parseColor("#abb2bf"),
        Color.parseColor("#21252b"),
        Color.parseColor("#21252b"),
        Color.parseColor("#282c34"),
        Color.parseColor("#61afef")
    );

    // color vars
    private final int text;
    private final int header;
    private final int background;
    private final int backgroundSec;
    private final int accent;

    // font styling
    private static final Typeface FONT = Typeface.MONOSPACE;
    private static final float TITLE_FS = 24f;
    private static final float TEXT_FS = 16f;

    /**
     * Constructor
     *
     * @param text The text color
     * @param header The header color
     * @param background The background color
     * @param backgroundSec The background secondary color
     * @param accent The accent color
     */
    CTheme(int text, int header, int background, int backgroundSec, int accent)
    {
        this.text = text;
        this.header = header;
        this.background = background;
        this.backgroundSec = backgroundSec;
        this.accent = accent;
    }

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
}
