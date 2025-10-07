package com.hbtipcalc.tipcalculator.view.pages;

import android.content.Context;
import android.view.View;

/**
 * This will be a "page" of the app.
 */
public abstract class BasePage
{
    protected Context ctx;

    /**
     * Constructor.
     *
     * @param ctx The context
     */
    public BasePage(Context ctx)
    {
        this.ctx = ctx;
    }

    // Getter for the view
    public abstract View getView();
}
