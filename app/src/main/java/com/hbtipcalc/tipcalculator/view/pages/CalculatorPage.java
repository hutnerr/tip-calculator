package com.hbtipcalc.tipcalculator.view.pages;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbtipcalc.tipcalculator.MainActivity;
import com.hbtipcalc.tipcalculator.R;
import com.hbtipcalc.tipcalculator.controllers.Calculator;
import com.hbtipcalc.tipcalculator.models.CTheme;
import com.hbtipcalc.tipcalculator.models.CalculatorApp;
import com.hbtipcalc.tipcalculator.view.elements.ElementContainer;
import com.hbtipcalc.tipcalculator.view.elements.ElementValueContainer;
import com.hbtipcalc.tipcalculator.view.elements.Header;
import com.hbtipcalc.tipcalculator.view.elements.IconButton;
import com.hbtipcalc.tipcalculator.view.elements.KeyValueText;
import com.hbtipcalc.tipcalculator.view.elements.Slider;
import com.hbtipcalc.tipcalculator.view.elements.SliderElementValueContainer;

/**
 * This is the main page that the app will rest on.
 */
public class CalculatorPage extends BasePage {

    private LinearLayout layout;
    private Header header;
    private IconButton splitBtn;
    private IconButton settingsBtn;
    private TextView tipResults;
    private TextView totalResults;

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

        layout = new LinearLayout(ctx);
        layout.setOrientation(LinearLayout.VERTICAL);

        // maybe i can wrap all this stuff here in a build() func or something to make it easier

        this.header = new Header(ctx, "Tip Calculator");
        generateSplitBtn();
        generateSettingsBtn();
        layout.addView(this.header);

        generateBillAmountField();
        generateTipPercentField();

        generateResultsField();
    }

    @Override
    public View getView()
    {
        return layout;
    }

    /**
     * Toggles the view of the splitSlider and splitTextField.
     * Basically enables / disables them being visible.
     * Controlled by splitBtn
     */
    public void toggleSplit()
    {
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
                ((MainActivity) ctx).setPage(new Settings((MainActivity) ctx));
            }
        });
        header.addIconButton(settingsBtn);
    }

    // TODO: i want this to have a clear button on the right
    // maybe make this a EditTextButton object that I create or something?
    private void generateBillAmountField()
    {
        EditText input = new EditText(ctx);
        input.setBackground(null); // remove the default bg
        input.setTextColor(t.getTextColor());
        // for now allow these inputs instead of static numbers
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        ElementContainer container = new ElementContainer(ctx, "Bill Amount", input);
        layout.addView(container);
    }

    // TODO: eventually have this match based on the user settings.
    private void generateTipPercentField()
    {
        Slider slider = new Slider(ctx);
        slider.setBounds(50, true);

        SliderElementValueContainer container = new SliderElementValueContainer(ctx, "Tip Percent", slider);
        layout.addView(container);

        slider.addObserver(container);
        slider.addObserver(this.calculator);
        container.setValue(slider.getProgress() + "%"); // set initially
    }

    private void generateResultsField()
    {
        // this is the container for both of them
        LinearLayout resultsContainer = new LinearLayout(ctx);
        resultsContainer.setOrientation(LinearLayout.VERTICAL);

        resultsContainer.addView(new KeyValueText(ctx, "Tip Amount", "0.54", true));
        resultsContainer.addView(new KeyValueText(ctx, "Total Amount", "5.42", true));
        // TODO: I should create 2 subclasses of this. One of the tip amount, one for the total amount
        // and they implement the CalculatorObserver differently.

        layout.addView(resultsContainer);
    }
}

// I think I want to make a general Observer interface that I can use more easily.
// maybe with Generics

// I also want to keep my more generic design.
// So for the thing that implements the listen to slider interface, it should be a separate object
// because not all of them are going to want to listen to a slider in that way for example
// maybe SliderContainer or something

// I also want to extend EditText to make my own TextBox so I can create my own custom observers for that
// as well.
