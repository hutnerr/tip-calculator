package com.hbtipcalc.tipcalculator.view.pages;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbtipcalc.tipcalculator.MainActivity;
import com.hbtipcalc.tipcalculator.R;
import com.hbtipcalc.tipcalculator.controllers.Calculator;
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
    private CalculatorApp app;

    private LinearLayout layout;
    private Header header;

    private IconButton exitBtn;
    private IconButton resetBtn;
    private IconButton helpBtn;

    private final String MY_WEBSITE_URL = "https://hunter-baker.com";
    private final String HELP_URL = "https://www.hunter-baker.com/pages/other/tip-calculator-help.html";
    private final String GITHUB_URL = "https://github.com/hutnerr/tip-calculator";
    private final String KOFI_URL = "https://ko-fi.com/hutner";

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

        CalculatorApp app = (CalculatorApp) ctx.getApplicationContext();
        this.app = app;

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

        generateFooterLinks();

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
                "¥",
                "£",
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
     * Creates and configures the footer links section.
     * Displays clickable links for GitHub repository, website, and Ko-fi support.
     */
    private void generateFooterLinks()
    {
        // spacer to push footer to bottom
        View flexSpacer = new View(ctx);
        LinearLayout.LayoutParams flexSpacerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1.0f
        );
        flexSpacer.setLayoutParams(flexSpacerParams);
        layout.addView(flexSpacer);

        LinearLayout footerContainer = new LinearLayout(ctx);
        footerContainer.setOrientation(LinearLayout.HORIZONTAL);
        footerContainer.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams footerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int footerMargin = dpToPx(16);
        footerParams.setMargins(footerMargin, footerMargin, footerMargin, footerMargin);
        footerContainer.setLayoutParams(footerParams);

        TextView githubLink = createFooterLink("GitHub");
        githubLink.setOnClickListener(v -> openUrl(GITHUB_URL));
        footerContainer.addView(githubLink);

        footerContainer.addView(createSeparator());

        TextView websiteLink = createFooterLink("My Website");
        websiteLink.setOnClickListener(v -> openUrl(MY_WEBSITE_URL));
        footerContainer.addView(websiteLink);

        footerContainer.addView(createSeparator());

        TextView kofiLink = createFooterLink("Support Me");
        kofiLink.setOnClickListener(v -> openUrl(KOFI_URL));
        footerContainer.addView(kofiLink);

        layout.addView(footerContainer);
    }

    /**
     * Creates a styled TextView for footer links.
     *
     * @param text The link text to display
     * @return Configured TextView for the footer link
     */
    private TextView createFooterLink(String text)
    {
        TextView link = new TextView(ctx);
        link.setText(text);
        link.setTextSize(14);
        link.setTextColor(this.app.getCTheme().getAccentColor());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        link.setLayoutParams(params);

        // Add some padding for easier tapping
        int padding = dpToPx(8);
        link.setPadding(padding, padding, padding, padding);

        return link;
    }

    /**
     * Creates a separator TextView (pipe character) between footer links.
     *
     * @return TextView configured as a separator
     */
    private TextView createSeparator()
    {
        TextView separator = new TextView(ctx);
        separator.setText(" | ");
        separator.setTextSize(14);
        separator.setTextColor(this.app.getCTheme().getTextColor());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        separator.setLayoutParams(params);

        return separator;
    }

    /**
     * Opens a URL in the default browser.
     *
     * @param url The URL to open
     */
    private void openUrl(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        ctx.startActivity(intent);
    }

    /**
     * Converts density-independent pixels to actual pixels.
     *
     * @param dp The dp value to convert
     * @return The pixel value
     */
    private int dpToPx(int dp)
    {
        float density = ctx.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
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