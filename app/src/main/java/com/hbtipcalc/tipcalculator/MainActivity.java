package com.hbtipcalc.tipcalculator;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.hbtipcalc.tipcalculator.models.CTheme;
import com.hbtipcalc.tipcalculator.models.CalculatorApp;
import com.hbtipcalc.tipcalculator.models.Settings;
import com.hbtipcalc.tipcalculator.view.pages.BasePage;
import com.hbtipcalc.tipcalculator.view.pages.CalculatorPage;

/**
 * This is the entry point of the app.
 *
 * @author Hunter Baker
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    private ViewGroup rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // root container for the pages
        rootLayout = new android.widget.FrameLayout(this);
        rootLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        rootLayout.setBackgroundColor(((CalculatorApp) getApplication()).getCTheme().getBackgroundColor());

        // this is how you can access the app
        // CalculatorApp app = (CalculatorApp) getApplication();
        // Settings settings = app.getSettings();
        // CTheme theme = app.getCTheme();

        setContentView(rootLayout);
        setPage(new CalculatorPage(this)); // out home page is the calculator
    }

    /**
     * Helper method to change the current page.
     *
     * @param page The page object we want the view to be now.
     */
    public void setPage(BasePage page)
    {
        rootLayout.removeAllViews();
        rootLayout.addView(page.getView());
    }

    /**
     * Reloads the activity. Used when UI changes, etc...
     */
    public void reload()
    {
        recreate();
    }
}
