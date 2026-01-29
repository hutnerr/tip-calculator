package com.hbtipcalc.tipcalculator.view.pages;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.hbtipcalc.tipcalculator.MainActivity;
import com.hbtipcalc.tipcalculator.R;
import com.hbtipcalc.tipcalculator.models.CTheme;
import com.hbtipcalc.tipcalculator.models.CalculatorApp;
import com.hbtipcalc.tipcalculator.models.RoundingFlag;
import com.hbtipcalc.tipcalculator.settings.Settings;
import com.hbtipcalc.tipcalculator.view.elements.DropDown;
import com.hbtipcalc.tipcalculator.view.elements.ElementContainer;
import com.hbtipcalc.tipcalculator.view.elements.Header;
import com.hbtipcalc.tipcalculator.view.elements.IconButton;
import com.hbtipcalc.tipcalculator.view.elements.Slider;
import com.hbtipcalc.tipcalculator.view.elements.SliderElementValueContainer;
import com.hbtipcalc.tipcalculator.view.elements.ThemeDropDown;

/**
 * A place for users to change their settings.
 */
public class SettingsPage extends BasePage
{
    private LinearLayout layout;

    private Header header;

    private IconButton exitBtn;
    private IconButton resetBtn;
    private IconButton helpBtn;

    /**
     * Constructor.
     *
     * @param ctx
     */
    public SettingsPage(Context ctx)
    {
        super(ctx);

        layout = new LinearLayout(ctx);
        layout.setOrientation(LinearLayout.VERTICAL);

        this.header = new Header(ctx, "Settings");
//        generateHelpBtn();
        generateResetBtn();
        generateExitBtn();
        layout.addView(header);

        generateDefaultTipOption();
        generateRoundingOption();
        generateCurrencyOption();
        generateThemeOption();
    }

    @Override
    public View getView()
    {
        return layout;
    }

    // BELOW ARE HELPER METHODS TO GENERATE UI CHUNKS
    private void generateExitBtn()
    {
        if (header == null) return;
        this.exitBtn = new IconButton(ctx, R.drawable.close);
        this.exitBtn.setOnClickListener(v -> {
            if (ctx instanceof MainActivity) {
                ((MainActivity) ctx).setPage(new CalculatorPage((MainActivity) ctx));
            }
        });
        header.addIconButton(exitBtn);
    }

    private void generateHelpBtn()
    {
        if (header == null) return;
        this.helpBtn = new IconButton(ctx, R.drawable.help);
        header.addIconButton(helpBtn);
    }

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
                        if (ctx instanceof android.app.Activity) {
                            android.app.Activity activity = (android.app.Activity) ctx;
                            activity.recreate();
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .setCancelable(true)
                    .show();
        });
    }


    private void generateDefaultTipOption()
    {
        Slider slider = new Slider(ctx);
        slider.setBounds(0, 50, true);

        SliderElementValueContainer container = new SliderElementValueContainer(ctx, "Default Tip Percent", slider, "%");
        layout.addView(container);

        int defaultTipPc = Settings.getInstance().getTipPercentage();
        slider.setProgress(defaultTipPc);
        slider.addObserver(container);
        slider.addObserver(value -> Settings.getInstance().setTipPercentage(value));

        container.setValue(defaultTipPc + "%");
    }

    private void generateRoundingOption()
    {
        DropDown dropDown = new DropDown(ctx);

        String[] options = {"Always Up", "Always Down", "Dynamic", "None"};
        dropDown.setItems(options);

        RoundingFlag currentFlag = Settings.getInstance().getRoundFlag();
        dropDown.setSelection(currentFlag.ordinal()); // ordinal() gives enum position

        dropDown.addObserver((position, value) -> {
            RoundingFlag flag = RoundingFlag.values()[position];
            Settings.getInstance().setRoundFlag(flag);

            CalculatorApp app = (CalculatorApp) ctx.getApplicationContext();
            app.getCalculator().setRoundingFlag(flag);
        });

        ElementContainer container = new ElementContainer(ctx, "Rounding Behavior", dropDown);
        layout.addView(container);
    }

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

    private void generateThemeOption()
    {
        ThemeDropDown themeDropDown = new ThemeDropDown(ctx);

        // Set all available themes
        themeDropDown.setThemes(CTheme.values());

        // Set the current theme as selected
        CalculatorApp app = (CalculatorApp) ctx.getApplicationContext();
        CTheme currentTheme = app.getCTheme();
        themeDropDown.setSelectedTheme(currentTheme);

        // Add observer to handle theme changes
        themeDropDown.addObserver((position, value) -> {
            CTheme newTheme = themeDropDown.getSelectedTheme();

            // Save the new theme to settings
            Settings.getInstance().setTheme(newTheme);
            app.setCTheme(newTheme);

            // Restart the activity to apply the theme
            if (ctx instanceof android.app.Activity) {
                android.app.Activity activity = (android.app.Activity) ctx;
                activity.recreate(); // This restarts the activity
            }
        });

        ElementContainer container = new ElementContainer(ctx, "Theme", themeDropDown);
        layout.addView(container);
    }
}
