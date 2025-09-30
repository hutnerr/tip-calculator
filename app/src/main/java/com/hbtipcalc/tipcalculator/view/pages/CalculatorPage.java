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
import com.hbtipcalc.tipcalculator.styles.StyleConstants;
import com.hbtipcalc.tipcalculator.view.elements.ElementContainer;
import com.hbtipcalc.tipcalculator.view.elements.Header;
import com.hbtipcalc.tipcalculator.view.elements.IconButton;
import com.hbtipcalc.tipcalculator.view.elements.KeyValueText;
import com.hbtipcalc.tipcalculator.view.elements.Slider;

public class CalculatorPage extends BasePage {

    private LinearLayout layout;

    private Header header;
    private IconButton splitBtn;
    private IconButton settingsBtn;
    private TextView tipResults;
    private TextView totalResults;

    public CalculatorPage(Context ctx)
    {
        super(ctx);

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
        input.setTextColor(StyleConstants.COLOR_TEXT);
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

        ElementContainer container = new ElementContainer(ctx, "Tip Percent", slider);
        layout.addView(container);

        // listener updates the value
        // FIXME: I changed this so it can have multiple listeners, so this needs to me changed
        slider.setOnSliderChangeListener(progress -> { container.setValue(progress + "%"); });
        container.setValue(slider.getProgress() + "%"); // set initially
    }

    private void generateResultsField()
    {
        // this is the container for both of them
        LinearLayout resultsContainer = new LinearLayout(ctx);
        resultsContainer.setOrientation(LinearLayout.VERTICAL);

        resultsContainer.addView(new KeyValueText(ctx, "Tip Amount", "0.54", true));
        resultsContainer.addView(new KeyValueText(ctx, "Total Amount", "5.42", true));

        // TODO: these need to be added to the sliders listeners so the values can be changes.

        layout.addView(resultsContainer);
    }
}
