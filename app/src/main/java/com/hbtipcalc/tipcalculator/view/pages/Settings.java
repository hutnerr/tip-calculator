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

public class Settings extends BasePage {

    private LinearLayout layout;

    private IconButton exitBtn;
    private IconButton resetBtn;
    private IconButton helpBtn;

    public Settings(Context ctx)
    {
        super(ctx);

        layout = new LinearLayout(ctx);
        layout.setOrientation(LinearLayout.VERTICAL);

        generateExitBtn();
        generateHelpBtn();
        generateResetBtn();

        Header header = new Header(ctx, "Settings");
        header.addIconButton(helpBtn);
        header.addIconButton(resetBtn);
        header.addIconButton(exitBtn);

        layout.addView(header);
    }

    @Override
    public View getView()
    {
        return layout;
    }

    private void generateExitBtn()
    {
        this.exitBtn = new IconButton(ctx, R.drawable.close);
        this.exitBtn.setOnClickListener(v -> {
            if (ctx instanceof MainActivity) {
                ((MainActivity) ctx).setPage(new Calculator((MainActivity) ctx));
            }
        });
    }

    private void generateHelpBtn()
    {
        this.helpBtn = new IconButton(ctx, R.drawable.help);
    }

    private void generateResetBtn()
    {
        this.resetBtn = new IconButton(ctx, R.drawable.reset);
    }
}
