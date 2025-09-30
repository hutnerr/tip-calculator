package com.hbtipcalc.tipcalculator.view.pages;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.hbtipcalc.tipcalculator.MainActivity;
import com.hbtipcalc.tipcalculator.R;
import com.hbtipcalc.tipcalculator.view.elements.Header;
import com.hbtipcalc.tipcalculator.view.elements.IconButton;

public class Settings extends BasePage {

    private LinearLayout layout;

    private Header header;

    private IconButton exitBtn;
    private IconButton resetBtn;
    private IconButton helpBtn;

    public Settings(Context ctx)
    {
        super(ctx);

        layout = new LinearLayout(ctx);
        layout.setOrientation(LinearLayout.VERTICAL);

        this.header = new Header(ctx, "Settings");
        generateHelpBtn();
        generateResetBtn();
        generateExitBtn();
        layout.addView(header);
    }

    @Override
    public View getView()
    {
        return layout;
    }

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
    }
}
