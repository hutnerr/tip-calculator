package com.hbtipcalc.tipcalculator.view.pages;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbtipcalc.tipcalculator.MainActivity;
import com.hbtipcalc.tipcalculator.R;
import com.hbtipcalc.tipcalculator.view.elements.Header;
import com.hbtipcalc.tipcalculator.view.elements.IconButton;

public class Calculator extends BasePage {

    private LinearLayout layout;

    private IconButton splitBtn;
    private IconButton settingsBtn;

    public Calculator(Context ctx)
    {
        super(ctx);

        layout = new LinearLayout(ctx);
        layout.setOrientation(LinearLayout.VERTICAL);

        generateSettingsBtn();
        generateSplitBtn();

        Header header = new Header(ctx, "Tip Calculator");
        header.addIconButton(splitBtn);
        header.addIconButton(settingsBtn);

        layout.addView(header);
    }

    @Override
    public View getView()
    {
        return layout;
    }

    private void generateSplitBtn()
    {
        this.splitBtn = new IconButton(ctx, R.drawable.people);
        this.splitBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(ctx)
                    .setTitle("Button Clicked")
                    .setMessage("The split button is working!")
                    .setPositiveButton("OK", null)
                    .show();
        });
    }

    private void generateSettingsBtn() {
        this.settingsBtn = new IconButton(ctx, R.drawable.settings);

        this.settingsBtn.setOnClickListener(v -> {
            if (ctx instanceof MainActivity) {
                ((MainActivity) ctx).setPage(new Settings((MainActivity) ctx));
            }
        });
    }
}
