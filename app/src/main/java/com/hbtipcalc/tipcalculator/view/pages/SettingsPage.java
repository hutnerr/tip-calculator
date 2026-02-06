package com.hbtipcalc.tipcalculator.view.pages;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;

import com.hbtipcalc.tipcalculator.MainActivity;
import com.hbtipcalc.tipcalculator.R;
import com.hbtipcalc.tipcalculator.models.CTheme;
import com.hbtipcalc.tipcalculator.models.CalculatorApp;
import com.hbtipcalc.tipcalculator.models.RoundingFlag;
import com.hbtipcalc.tipcalculator.settings.Settings;
import com.hbtipcalc.tipcalculator.util.Clogger;
import com.hbtipcalc.tipcalculator.view.elements.DropDown;
import com.hbtipcalc.tipcalculator.view.elements.ElementContainer;
import com.hbtipcalc.tipcalculator.view.elements.Header;
import com.hbtipcalc.tipcalculator.view.elements.IconButton;
import com.hbtipcalc.tipcalculator.view.elements.Slider;
import com.hbtipcalc.tipcalculator.view.elements.SliderElementValueContainer;

/**
 * Settings page for configuring application preferences.
 * Allows users to customize theme, rounding behavior, currency symbol,
 * and default tip percentage. Includes reset functionality to restore defaults.
 */
public class SettingsPage extends BasePage
{
    private LinearLayout layout;

    private Header header;

    private IconButton exitBtn;
    private IconButton resetBtn;
    private IconButton helpBtn;

    private final String HELP_URL = "https://hunter-baker.com";

    /**
     * Constructs a new SettingsPage.
     * Initializes all settings options including theme, rounding, currency,
     * and default tip percentage.
     *
     * @param ctx The application context
     */
    public SettingsPage(Context ctx)
    {
        super(ctx);

        layout = new LinearLayout(ctx);
        layout.setOrientation(LinearLayout.VERTICAL);

        this.header = new Header(ctx, "Settings");
        generateHelpBtn();
        generateResetBtn();
        generateExitBtn();
        layout.addView(header);

        generateThemeOption();
        generateRoundingOption();
        generateCurrencyOption();
        generateDefaultTipOption();

        Clogger.info("Settings page loaded successfully");
    }

    /**
     * Returns the root view for this page.
     *
     * @return The LinearLayout containing all settings content
     */
    @Override
    public View getView()
    {
        return layout;
    }

    /**
     * Creates and configures the exit button in the header.
     * When clicked, navigates back to the calculator page.
     */
    private void generateExitBtn()
    {
        if (header == null) return;
        this.exitBtn = new IconButton(ctx, R.drawable.close);
        this.exitBtn.setOnClickListener(v -> {
            if (ctx instanceof MainActivity)
            {
                ((MainActivity) ctx).setPage(new CalculatorPage((MainActivity) ctx));
            }
        });
        header.addIconButton(exitBtn);
    }

    /**
     * Creates and configures the help button in the header.
     * Currently unused but reserved for future help functionality.
     */
    private void generateHelpBtn()
    {
        if (header == null) return;
        this.helpBtn = new IconButton(ctx, R.drawable.help);
        this.helpBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(HELP_URL));
            ctx.startActivity(intent);
        });
        header.addIconButton(helpBtn);
    }

    /**
     * Creates and configures the reset button in the header.
     * Shows confirmation dialog before resetting all settings to defaults
     * and recreating the activity to apply changes.
     */
    private void generateResetBtn()
    {
        if (header == null) return;

        this.resetBtn = new IconButton(ctx, R.drawable.reset);
        header.addIconButton(resetBtn);

        resetBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(ctx)
                    .setTitle("Reset Settings")
                    .setMessage("Are you sure you want to revert back to default settings?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Settings.getInstance().reset();
                        CalculatorApp app = (CalculatorApp) ctx.getApplicationContext();
                        app.setCTheme(Settings.getInstance().getTheme());
                        if (ctx instanceof android.app.Activity)
                        {
                            android.app.Activity activity = (android.app.Activity) ctx;
                            activity.recreate();
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .setCancelable(true)
                    .show();
        });
    }

    /**
     * Creates and configures the default tip percentage slider.
     * Allows users to set their preferred default tip percentage from 0% to 50%.
     * Updates are saved to settings immediately.
     */
    private void generateDefaultTipOption()
    {
        Slider slider = new Slider(ctx, "tipsetting");
        slider.setBounds(0, 50, true);

        SliderElementValueContainer container = new SliderElementValueContainer(ctx, "Default Tip Percent", slider, "%");
        layout.addView(container);

        int defaultTipPc = Settings.getInstance().getTipPercentage();
        slider.setProgress(defaultTipPc);
        slider.addObserver(container);
        slider.addObserver((value, id) -> Settings.getInstance().setTipPercentage(value));

        container.setValue(defaultTipPc + "%");
    }

    /**
     * Creates and configures the rounding behavior dropdown.
     * Allows users to select how tip amounts should be rounded:
     * Always Up, Always Down, Dynamic, or None.
     * Updates are saved to settings and applied to the calculator immediately.
     */
    private void generateRoundingOption()
    {
        DropDown dropDown = new DropDown(ctx);

        String[] options = {"Always Up", "Always Down", "Dynamic", "None"};
        dropDown.setItems(options);

        RoundingFlag currentFlag = Settings.getInstance().getRoundFlag();
        dropDown.setSelection(currentFlag.ordinal());

        dropDown.addObserver((position, value) -> {
            RoundingFlag flag = RoundingFlag.values()[position];
            Settings.getInstance().setRoundFlag(flag);

            CalculatorApp app = (CalculatorApp) ctx.getApplicationContext();
            app.getCalculator().setRoundingFlag(flag);
        });

        ElementContainer container = new ElementContainer(ctx, "Rounding Behavior", dropDown);
        layout.addView(container);
    }

    /**
     * Creates and configures the currency symbol dropdown.
     * Allows users to select their preferred currency symbol from
     * common options including $, €, £, ₹, ₣, R, and kr.
     * Updates are saved to settings immediately.
     */
    private void generateCurrencyOption()
    {
        DropDown dropDown = new DropDown(ctx);

        String[] currencies = {
                "$",
                "€",
                "£",
                "₹",
                "₣",
                "R",
                "kr"
        };

        dropDown.setItems(currencies);

        String currentCurrency = Settings.getInstance().getCurrency();
        for (int i = 0; i < currencies.length; i++)
        {
            if (currencies[i].startsWith(currentCurrency))
            {
                dropDown.setSelection(i);
                break;
            }
        }

        dropDown.addObserver((position, value) -> {
            Settings.getInstance().setCurrency(value);
        });

        ElementContainer container = new ElementContainer(ctx, "Currency Symbol", dropDown);
        layout.addView(container);
    }

    /**
     * Creates and configures the theme selection dropdown.
     * Allows users to select from available app themes.
     * When changed, saves the theme to settings and recreates the activity
     * to apply the new theme.
     */
    private void generateThemeOption()
    {
        DropDown themeDropDown = new DropDown(ctx);

        // Get all themes and format their names
        CTheme[] allThemes = CTheme.values();
        String[] themeNames = new String[allThemes.length];
        for (int i = 0; i < allThemes.length; i++)
        {
            themeNames[i] = formatThemeName(allThemes[i].name());
        }

        themeDropDown.setItems(themeNames);

        // Set the current theme as selected
        CalculatorApp app = (CalculatorApp) ctx.getApplicationContext();
        CTheme currentTheme = app.getCTheme();

        // Find and set the current theme
        for (int i = 0; i < allThemes.length; i++)
        {
            if (allThemes[i] == currentTheme)
            {
                themeDropDown.setSelection(i);
                break;
            }
        }

        // Add observer to handle theme changes
        themeDropDown.addObserver((position, value) -> {
            CTheme newTheme = allThemes[position];

            // Save the new theme to settings
            Settings.getInstance().setTheme(newTheme);
            app.setCTheme(newTheme);

            // Restart the activity to apply the theme
            if (ctx instanceof android.app.Activity)
            {
                android.app.Activity activity = (android.app.Activity) ctx;
                activity.recreate();
            }
        });

        ElementContainer container = new ElementContainer(ctx, "Theme", themeDropDown);
        layout.addView(container);
    }

    /**
     * Formats an enum name into a user-friendly display string.
     * Converts underscores to spaces and capitalizes the first letter of each word.
     *
     * @param name The enum name to format (e.g., "DARK_MODE")
     * @return The formatted name (e.g., "Dark Mode")
     */
    private String formatThemeName(String name)
    {
        String[] words = name.split("_");
        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < words.length; i++)
        {
            String word = words[i];
            formatted.append(word.charAt(0))
                    .append(word.substring(1).toLowerCase());

            if (i < words.length - 1)
            {
                formatted.append(" ");
            }
        }

        return formatted.toString();
    }
}