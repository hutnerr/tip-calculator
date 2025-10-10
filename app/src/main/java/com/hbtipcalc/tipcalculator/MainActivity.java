package com.hbtipcalc.tipcalculator;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hbtipcalc.tipcalculator.models.CalculatorApp;
import com.hbtipcalc.tipcalculator.view.pages.BasePage;
import com.hbtipcalc.tipcalculator.view.pages.CalculatorPage;
import com.hbtipcalc.tipcalculator.view.pages.SettingsPage;

/**
 * This is the entry point of the app.
 *
 * @author Hunter Baker
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity
{
    private static final String KEY_CURRENT_PAGE = "current_page";
    private String currentPage = "calculator"; // default

    private ViewGroup rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // root container for the pages
        rootLayout = new FrameLayout(this);
        rootLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        rootLayout.setBackgroundColor(((CalculatorApp) getApplication()).getCTheme().getBackgroundColor());

        setContentView(rootLayout);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        ViewCompat.setOnApplyWindowInsetsListener(rootLayout, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        if (savedInstanceState != null) currentPage = savedInstanceState.getString(KEY_CURRENT_PAGE, "calculator");

        if (currentPage.equals("settings")) setPage(new SettingsPage(this));
        else setPage(new CalculatorPage(this));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CURRENT_PAGE, currentPage);
    }

    public void setPage(BasePage page) {
        if (page instanceof SettingsPage)
            currentPage = "settings";
        else
            currentPage = "calculator";

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
