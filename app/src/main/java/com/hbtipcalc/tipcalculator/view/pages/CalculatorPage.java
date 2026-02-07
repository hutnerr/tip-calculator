package com.hbtipcalc.tipcalculator.view.pages;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbtipcalc.tipcalculator.MainActivity;
import com.hbtipcalc.tipcalculator.R;
import com.hbtipcalc.tipcalculator.controllers.Calculator;
import com.hbtipcalc.tipcalculator.models.CTheme;
import com.hbtipcalc.tipcalculator.models.CalculatorApp;
import com.hbtipcalc.tipcalculator.settings.Settings;
import com.hbtipcalc.tipcalculator.util.Clogger;
import com.hbtipcalc.tipcalculator.view.elements.ElementContainer;
import com.hbtipcalc.tipcalculator.view.elements.ElementValueContainer;
import com.hbtipcalc.tipcalculator.view.elements.Header;
import com.hbtipcalc.tipcalculator.view.elements.IconButton;
import com.hbtipcalc.tipcalculator.view.elements.KeyValueText;
import com.hbtipcalc.tipcalculator.view.elements.NumPad;
import com.hbtipcalc.tipcalculator.view.elements.Slider;
import com.hbtipcalc.tipcalculator.view.elements.SliderElementValueContainer;
import com.hbtipcalc.tipcalculator.view.elements.SplitKeyValueText;
import com.hbtipcalc.tipcalculator.view.elements.TextBox;
import com.hbtipcalc.tipcalculator.view.elements.TipKeyValueText;
import com.hbtipcalc.tipcalculator.view.elements.TotalKeyValueText;

/**
 * Main calculator page for the tip calculator application.
 * Displays input fields for bill amount and tip percentage, optional split
 * functionality, and calculated results including tip, total, and split amounts.
 */
public class CalculatorPage extends BasePage {
    private FrameLayout root;
    private LinearLayout layout;
    private Header header;
    private IconButton splitBtn;
    private IconButton settingsBtn;
    private TextBox billAmount;

    private Slider splitSlider;

    private SliderElementValueContainer splitSliderContainer;
    private SplitKeyValueText splitKeyValueText;

    private CTheme t;
    private Calculator calculator;

    private CalculatorApp app;

    /**
     * Constructs a new CalculatorPage.
     * Initializes all UI components including header, input fields, sliders,
     * results display, and numpad. Restores previous state from settings.
     *
     * @param ctx The application context
     */
    public CalculatorPage(Context ctx) {
        super(ctx);

        CalculatorApp app = (CalculatorApp) ctx.getApplicationContext();
        this.app = app;
        this.t = app.getCTheme();
        this.calculator = app.getCalculator();

        root = new FrameLayout(ctx);

        layout = new LinearLayout(ctx);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        this.header = new Header(ctx, "Tip Calculator");
        generateSplitBtn();
        generateSettingsBtn();
        layout.addView(this.header);

        generateBillAmountField();
        generateTipPercentField();
        generateShareField();
        generateResultsField();

        root.addView(layout);

        NumPad numPad = new NumPad(ctx);
        FrameLayout.LayoutParams numPadParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        numPadParams.gravity = Gravity.BOTTOM;
        numPad.setLayoutParams(numPadParams);

        numPad.addObserver(billAmount);

        splitSlider.setProgress(calculator.getSplitCount());

        root.addView(numPad);

        double temp = calculator.getBillAmt().doubleValue();
        billAmount.setValue(temp);

        if (!this.app.getSettings().isSplitActive())
        {
            this.splitSliderContainer.setVisibility(View.GONE);
            this.splitKeyValueText.setVisibility(View.GONE);
        }

        calculator.calculate();
        Clogger.info("Calculator Page loaded successfully");
    }

    /**
     * Returns the root view for this page.
     *
     * @return The FrameLayout containing all page content
     */
    @Override
    public View getView() {
        return root;
    }

    /**
     * Toggles the visibility of the split functionality with animation.
     * Shows or hides the split slider and split amount result field.
     * Updates the split active state in settings.
     */
    public void toggleSplit()
    {
        if (splitSliderContainer == null || splitKeyValueText == null) return;

        if (splitSliderContainer.getVisibility() == View.VISIBLE)
        {
            // Animate out
            splitSliderContainer.animate()
                    .alpha(0f)
                    .scaleY(0.8f)
                    .setDuration(200)
                    .setInterpolator(new android.view.animation.AccelerateInterpolator())
                    .withEndAction(() -> {
                        splitSliderContainer.setVisibility(View.GONE);
                        splitSliderContainer.setScaleY(1f);
                    })
                    .start();

            splitKeyValueText.animate()
                    .alpha(0f)
                    .scaleY(0.8f)
                    .setDuration(200)
                    .setInterpolator(new android.view.animation.AccelerateInterpolator())
                    .withEndAction(() -> {
                        splitKeyValueText.setVisibility(View.GONE);
                        splitKeyValueText.setScaleY(1f);
                    })
                    .start();

            this.app.getSettings().setSplitActive(false);
        }
        else
        {
            // Set initial state before animating in
            splitSliderContainer.setAlpha(0f);
            splitSliderContainer.setScaleY(0.8f);
            splitSliderContainer.setVisibility(View.VISIBLE);

            splitKeyValueText.setAlpha(0f);
            splitKeyValueText.setScaleY(0.8f);
            splitKeyValueText.setVisibility(View.VISIBLE);

            // Animate in
            splitSliderContainer.animate()
                    .alpha(1f)
                    .scaleY(1f)
                    .setDuration(200)
                    .setInterpolator(new android.view.animation.DecelerateInterpolator())
                    .start();

            splitKeyValueText.animate()
                    .alpha(1f)
                    .scaleY(1f)
                    .setDuration(200)
                    .setInterpolator(new android.view.animation.DecelerateInterpolator())
                    .start();

            this.app.getSettings().setSplitActive(true);
        }
    }

    /**
     * Creates and configures the split button in the header.
     * When clicked, toggles the split functionality visibility.
     */
    private void generateSplitBtn() {
        if (header == null) return;
        this.splitBtn = new IconButton(ctx, R.drawable.people);
        this.splitBtn.setOnClickListener(v -> {
            toggleSplit();
        });
        header.addIconButton(splitBtn);
    }

    /**
     * Creates and configures the settings button in the header.
     * When clicked, navigates to the settings page.
     */
    private void generateSettingsBtn() {
        if (header == null) return;
        this.settingsBtn = new IconButton(ctx, R.drawable.settings);
        this.settingsBtn.setOnClickListener(v -> {
            if (ctx instanceof MainActivity) {
                ((MainActivity) ctx).setPage(new SettingsPage((MainActivity) ctx));
            }
        });
        header.addIconButton(settingsBtn);
    }

    /**
     * Creates and configures the bill amount input field.
     * Connects the field to the calculator as an observer.
     */
    private void generateBillAmountField() {
        this.billAmount = new TextBox(ctx);
        billAmount.addObserver(calculator);
        ElementContainer container = new ElementContainer(ctx, "Bill Amount", billAmount);
        layout.addView(container);
    }

    /**
     * Creates and configures the split slider field.
     * Sets the range from 2 to 10 people and connects to the calculator.
     */
    private void generateShareField() {
        Slider slider = new Slider(ctx, "split");
        slider.setBounds(2, 10, false);
        slider.setProgress(0);

        this.splitSlider = slider;
        this.splitSliderContainer = new SliderElementValueContainer(ctx, "Split", slider, " People");
        layout.addView(splitSliderContainer);

        slider.addObserver(splitSliderContainer);
        slider.addObserver(calculator);

        splitSliderContainer.setValue(slider.getProgress() + " people");
    }

    /**
     * Creates and configures the tip percentage slider field.
     * Sets the range from 0% to 50% and initializes to the default tip percentage
     * from settings.
     */
    private void generateTipPercentField() {
        Slider slider = new Slider(ctx, "tip");
        slider.setBounds(0, 50, true);

        SliderElementValueContainer container = new SliderElementValueContainer(ctx, "Tip Percent", slider, "%");
        layout.addView(container);

        slider.addObserver(container);
        slider.addObserver(this.calculator);
        int defaultTipPc = Settings.getInstance().getTipPercentage();
        slider.setProgress(defaultTipPc);
        container.setValue(defaultTipPc + "%");
    }

    /**
     * Creates and configures the results display section.
     * Shows tip amount, total amount, and split amount per person.
     * Connects all result fields to the calculator as observers.
     */
    private void generateResultsField()
    {
        // this is the container for both of them
        LinearLayout resultsContainer = new LinearLayout(ctx);
        resultsContainer.setOrientation(LinearLayout.VERTICAL);

        TipKeyValueText tipText = new TipKeyValueText(ctx);
        TotalKeyValueText totalText = new TotalKeyValueText(ctx);

        this.splitKeyValueText = new SplitKeyValueText(ctx);

        resultsContainer.addView(tipText);
        resultsContainer.addView(totalText);
        resultsContainer.addView(splitKeyValueText);

        calculator.addObserver(tipText);
        calculator.addObserver(totalText);
        calculator.addObserver(splitKeyValueText);

        layout.addView(resultsContainer);
    }
}