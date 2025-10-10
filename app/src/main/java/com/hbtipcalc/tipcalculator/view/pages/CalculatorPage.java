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
 * This is the main page that the app will rest on.
 */
public class CalculatorPage extends BasePage
{
    private FrameLayout root;
    private LinearLayout layout;
    private Header header;
    private IconButton splitBtn;
    private IconButton settingsBtn;
    private TextBox billAmount;

    private SliderElementValueContainer splitSliderContainer;
    private SplitKeyValueText splitKeyValueText;

    private CTheme t;
    private Calculator calculator;

    /**
     * Constructor.
     *
     * @param ctx
     */
    public CalculatorPage(Context ctx)
    {
        super(ctx);

        CalculatorApp app = (CalculatorApp) ctx.getApplicationContext();
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

        root.addView(numPad);

        toggleSplit(); // SPLIT OFF BY DEFAULT FOR NOW
    }

    @Override
    public View getView()
    {
        return root;
    }

    /**
     * Toggles the view of the splitSlider and splitTextField.
     * Basically enables / disables them being visible.
     * Controlled by splitBtn
     */
    public void toggleSplit()
    {
        if (splitSliderContainer == null || splitKeyValueText == null) return;

        if (splitSliderContainer.getVisibility() == View.VISIBLE)
        {
            splitSliderContainer.setVisibility(View.GONE);
            splitKeyValueText.setVisibility(View.GONE);
        }
        else
        {
            splitSliderContainer.setVisibility(View.VISIBLE);
            splitKeyValueText.setVisibility(View.VISIBLE);
        }
    }

    // GENERATOR METHODS BELOW
    private void generateSplitBtn()
    {
        if (header == null) return;
        this.splitBtn = new IconButton(ctx, R.drawable.people);
        this.splitBtn.setOnClickListener(v -> { toggleSplit(); });
        header.addIconButton(splitBtn);
    }

    private void generateSettingsBtn()
    {
        if (header == null) return;
        this.settingsBtn = new IconButton(ctx, R.drawable.settings);
        this.settingsBtn.setOnClickListener(v -> {
            if (ctx instanceof MainActivity) {
                ((MainActivity) ctx).setPage(new SettingsPage((MainActivity) ctx));
            }
        });
        header.addIconButton(settingsBtn);
    }

    private void generateBillAmountField()
    {
        this.billAmount = new TextBox(ctx);
        billAmount.addObserver(calculator);
        ElementContainer container = new ElementContainer(ctx, "Bill Amount", billAmount);
        layout.addView(container);
    }

    private void generateShareField()
    {
        Slider slider = new Slider(ctx);
        slider.setBounds(2, 10, false);

        slider.setProgress(0); // 0, but would actually be 2 cause our minimum

        this.splitSliderContainer = new SliderElementValueContainer(ctx, "Split", slider, " people");
        layout.addView(splitSliderContainer);

        slider.addObserver(splitSliderContainer);
        splitSliderContainer.setValue(slider.getProgress() + " people");
    }

    // TODO: eventually have this match based on the user settings.
    private void generateTipPercentField()
    {
        Slider slider = new Slider(ctx);
        slider.setBounds(0, 50, true);

        SliderElementValueContainer container = new SliderElementValueContainer(ctx, "Tip Percent", slider, "%");
        layout.addView(container);

        slider.addObserver(container);
        slider.addObserver(this.calculator);
        int defaultTipPc = Settings.getInstance().getTipPercentage();
        slider.setProgress(defaultTipPc);
        container.setValue(defaultTipPc + "%"); // set initially
    }

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

        layout.addView(resultsContainer);
    }
}
