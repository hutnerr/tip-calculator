package com.hbtipcalc.tipcalculator.view.elements;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbtipcalc.tipcalculator.models.CalculatorApp;
import com.hbtipcalc.tipcalculator.models.CTheme;
import com.hbtipcalc.tipcalculator.models.ScreenProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Inline expanding dropdown — no PopupWindow, no floating coordinate math.
 * Tapping the header shows the item list directly below it in the same layout.
 */
public class DropDown extends LinearLayout
{
    private final List<DropDownObserver> observers;
    private final List<String> items;
    private final CTheme theme;
    private final ScreenProfile profile;

    private final TextView selectedView;
    private final TextView arrowView;
    private final LinearLayout selectedContainer;
    private final LinearLayout itemsContainer;

    private int selectedPosition = 0;
    private ValueAnimator currentAnimator;

    public DropDown(Context ctx)
    {
        super(ctx);

        this.observers = new ArrayList<>();
        this.items     = new ArrayList<>();

        CalculatorApp app = (CalculatorApp) ctx.getApplicationContext();
        this.theme   = app.getCTheme();
        this.profile = app.getScreenProfile();

        setOrientation(VERTICAL);
        setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        // ── Header row (always visible) ───────────────────────────────────────

        selectedContainer = new LinearLayout(ctx);
        selectedContainer.setOrientation(HORIZONTAL);
        selectedContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        selectedContainer.setGravity(Gravity.CENTER_VERTICAL);

        int p = profile.getDropdownItemPadding();
        selectedContainer.setPadding(p, p, p, p);
        applyHeaderBg(true);

        selectedView = new TextView(ctx);
        selectedView.setLayoutParams(new LinearLayout.LayoutParams(
                0, LayoutParams.WRAP_CONTENT, 1f));
        selectedView.setTextColor(theme.getTextColor());
        selectedView.setTextSize(profile.getTextFontSize());
        selectedView.setTypeface(theme.getFont());
        selectedView.setGravity(Gravity.CENTER_VERTICAL);
        selectedView.setText("Select...");

        arrowView = new TextView(ctx);
        arrowView.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        arrowView.setTextColor(theme.getTextColor());
        arrowView.setTextSize(profile.getTextFontSize());
        arrowView.setText("▼");
        arrowView.setPadding(dpToPx(8), 0, 0, 0);

        selectedContainer.addView(selectedView);
        selectedContainer.addView(arrowView);
        selectedContainer.setOnClickListener(v -> toggle());
        addView(selectedContainer);

        // ── Items list (inline, hidden until opened) ──────────────────────────

        itemsContainer = new LinearLayout(ctx);
        itemsContainer.setOrientation(VERTICAL);
        LinearLayout.LayoutParams listParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, 0); // height starts at 0
        itemsContainer.setLayoutParams(listParams);
        itemsContainer.setVisibility(GONE);
        itemsContainer.setClipToOutline(true);
        addView(itemsContainer);
    }

    // ── Open / close ──────────────────────────────────────────────────────────

    private void toggle()
    {
        if (itemsContainer.getVisibility() == VISIBLE) collapse();
        else expand();
    }

    private void expand()
    {
        // Build item views first so we can measure the true height
        itemsContainer.removeAllViews();

        View divider = new View(getContext());
        divider.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, dpToPx(1)));
        divider.setBackgroundColor(theme.getAccentColor());
        itemsContainer.addView(divider);

        for (int i = 0; i < items.size(); i++)
        {
            final int pos = i;
            boolean   sel = (pos == selectedPosition);

            TextView item = new TextView(getContext());
            item.setText(items.get(i));
            item.setTextSize(profile.getTextFontSize());
            item.setTypeface(theme.getFont());
            int pp = profile.getDropdownItemPadding();
            item.setPadding(pp, pp, pp, pp);
            item.setBackgroundColor(sel ? theme.getAccentColor() : theme.getBackgroundSecColor());
            item.setTextColor(sel ? theme.getBackgroundColor() : theme.getTextColor());
            item.setOnClickListener(v -> setSelection(pos));
            itemsContainer.addView(item);
        }

        GradientDrawable listBg = new GradientDrawable();
        listBg.setColor(theme.getBackgroundSecColor());
        listBg.setCornerRadii(new float[]{0,0, 0,0, r(8),r(8), r(8),r(8)});
        itemsContainer.setBackground(listBg);

        applyHeaderBg(false);

        // Measure the full height before making it visible
        itemsContainer.measure(
                View.MeasureSpec.makeMeasureSpec(getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );
        final int targetHeight = itemsContainer.getMeasuredHeight();

        final LinearLayout.LayoutParams lp =
                (LinearLayout.LayoutParams) itemsContainer.getLayoutParams();
        lp.height = 0;
        itemsContainer.setVisibility(VISIBLE);

        if (currentAnimator != null) currentAnimator.cancel();
        currentAnimator = ValueAnimator.ofInt(0, targetHeight);
        currentAnimator.setDuration(220);
        currentAnimator.setInterpolator(new DecelerateInterpolator());
        currentAnimator.addUpdateListener(va -> {
            lp.height = (int) va.getAnimatedValue();
            itemsContainer.setLayoutParams(lp);
        });
        currentAnimator.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator a) {
                lp.height = LayoutParams.WRAP_CONTENT;
                itemsContainer.setLayoutParams(lp);
            }
        });
        currentAnimator.start();

        arrowView.animate()
                .rotation(180f)
                .setDuration(220)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    private void collapse()
    {
        final int initialHeight = itemsContainer.getHeight();
        final LinearLayout.LayoutParams lp =
                (LinearLayout.LayoutParams) itemsContainer.getLayoutParams();

        if (currentAnimator != null) currentAnimator.cancel();
        currentAnimator = ValueAnimator.ofInt(initialHeight, 0);
        currentAnimator.setDuration(160);
        currentAnimator.setInterpolator(new AccelerateInterpolator());
        currentAnimator.addUpdateListener(va -> {
            lp.height = (int) va.getAnimatedValue();
            itemsContainer.setLayoutParams(lp);
        });
        currentAnimator.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator a) {
                itemsContainer.setVisibility(GONE);
                lp.height = LayoutParams.WRAP_CONTENT;
                itemsContainer.setLayoutParams(lp);
            }
        });
        currentAnimator.start();

        applyHeaderBg(true);

        arrowView.animate()
                .rotation(0f)
                .setDuration(160)
                .setInterpolator(new AccelerateInterpolator())
                .start();
    }

    /** Rounded-corner background for the header row. Flat bottom when open. */
    private void applyHeaderBg(boolean closed)
    {
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(theme.getBackgroundSecColor());
        if (closed)
            bg.setCornerRadius(r(8));
        else
            bg.setCornerRadii(new float[]{r(8),r(8), r(8),r(8), 0,0, 0,0});
        selectedContainer.setBackground(bg);
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public void setItems(String[] newItems)
    {
        this.items.clear();
        for (String s : newItems) this.items.add(s);
        if (newItems.length > 0 && selectedPosition < newItems.length)
            selectedView.setText(newItems[selectedPosition]);
    }

    public void setSelection(int position)
    {
        if (position < 0 || position >= items.size()) return;
        selectedPosition = position;
        selectedView.setText(items.get(position));
        collapse();
        notifyObservers(position, items.get(position));
    }

    public void addObserver(DropDownObserver obs)
    {
        if (obs != null && !observers.contains(obs)) observers.add(obs);
    }

    public void removeObserver(DropDownObserver obs)
    {
        observers.remove(obs);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void notifyObservers(int position, String value)
    {
        for (DropDownObserver o : observers) o.handleDropDownChange(position, value);
    }

    private int dpToPx(int dp)
    {
        return Math.round(dp * getContext().getResources().getDisplayMetrics().density);
    }

    private float r(int dp) { return dpToPx(dp); }
}
