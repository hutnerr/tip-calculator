package com.hbtipcalc.tipcalculator.view.elements;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbtipcalc.tipcalculator.models.CalculatorApp;
import com.hbtipcalc.tipcalculator.models.CTheme;
import com.hbtipcalc.tipcalculator.models.ScreenProfile;

import java.util.ArrayList;
import java.util.List;

public class QuickTipButtons extends FrameLayout
{
    private static final int[] PRESET_TIPS = {15, 18, 20, 25};

    private final CTheme t;
    private final ScreenProfile profile;
    private final List<SliderObserver> observers = new ArrayList<>();
    private final Button[] presetBtns = new Button[PRESET_TIPS.length];
    private final Button customBtn;
    private final View indicator;

    private int currentTip;

    public QuickTipButtons(Context ctx)
    {
        super(ctx);

        CalculatorApp app = (CalculatorApp) ctx.getApplicationContext();
        this.t = app.getCTheme();
        this.profile = app.getScreenProfile();

        setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        // Layer 1 (behind): sliding selection pill
        indicator = new View(ctx);
        GradientDrawable pillBg = new GradientDrawable();
        pillBg.setCornerRadius(dpToPx(8));
        pillBg.setColor(t.getAccentColor());
        indicator.setBackground(pillBg);
        addView(indicator, new FrameLayout.LayoutParams(0, 0));

        // Layer 2 (front): equal-weight button row
        LinearLayout btnRow = new LinearLayout(ctx);
        btnRow.setOrientation(LinearLayout.HORIZONTAL);
        btnRow.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        int margin = dpToPx(2);

        for (int i = 0; i < PRESET_TIPS.length; i++)
        {
            final int tip = PRESET_TIPS[i];
            Button btn = new Button(ctx);
            btn.setText(tip + "%");
            styleBtn(btn);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            params.setMargins(margin, 0, margin, 0);
            btn.setLayoutParams(params);
            btn.setOnClickListener(v -> selectTip(tip));
            presetBtns[i] = btn;
            btnRow.addView(btn);
        }

        customBtn = new Button(ctx);
        customBtn.setText("...");
        styleBtn(customBtn);
        LinearLayout.LayoutParams customParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        customParams.setMargins(margin, 0, margin, 0);
        customBtn.setLayoutParams(customParams);
        customBtn.setOnClickListener(v -> showCustomTipDialog());
        btnRow.addView(customBtn);

        addView(btnRow);
    }

    public void setCurrentTip(int tip)
    {
        this.currentTip = tip;
        refreshTextColors();
        post(() -> positionIndicator(getButtonIndex(tip), false));
    }

    public void addObserver(SliderObserver obs)
    {
        if (obs != null && !observers.contains(obs)) observers.add(obs);
    }

    public void removeObserver(SliderObserver obs)
    {
        observers.remove(obs);
    }

    private void selectTip(int tip)
    {
        this.currentTip = tip;
        refreshTextColors();
        positionIndicator(getButtonIndex(tip), true);
        notifyObservers(tip);
    }

    private int getButtonIndex(int tip)
    {
        for (int i = 0; i < PRESET_TIPS.length; i++) {
            if (PRESET_TIPS[i] == tip) return i;
        }
        return PRESET_TIPS.length; // custom button
    }

    private void positionIndicator(int index, boolean animate)
    {
        if (getWidth() == 0 || getHeight() == 0) {
            post(() -> positionIndicator(index, animate));
            return;
        }
        int numBtns = PRESET_TIPS.length + 1;
        float btnWidth = getWidth() / (float) numBtns;
        float targetX = index * btnWidth;

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) indicator.getLayoutParams();
        if (lp.width != (int) btnWidth || lp.height != getHeight()) {
            lp.width = (int) btnWidth;
            lp.height = getHeight();
            indicator.setLayoutParams(lp);
        }

        if (animate) {
            indicator.animate()
                    .x(targetX)
                    .setDuration(220)
                    .setInterpolator(new android.view.animation.OvershootInterpolator(0.8f))
                    .start();
        } else {
            indicator.setX(targetX);
        }
    }

    private void refreshTextColors()
    {
        boolean isPreset = false;
        for (int i = 0; i < PRESET_TIPS.length; i++) {
            boolean sel = (currentTip == PRESET_TIPS[i]);
            presetBtns[i].setTextColor(sel ? t.getBackgroundColor() : t.getTextColor());
            if (sel) isPreset = true;
        }
        customBtn.setTextColor(!isPreset ? t.getBackgroundColor() : t.getTextColor());
    }

    private void styleBtn(Button btn)
    {
        btn.setAllCaps(false);
        btn.setTypeface(t.getFont());
        btn.setTextSize(profile.getTextFontSize());
        btn.setTextColor(t.getTextColor());
        GradientDrawable transparent = new GradientDrawable();
        transparent.setColor(android.graphics.Color.TRANSPARENT);
        btn.setBackground(transparent);
        btn.setStateListAnimator(null);
        btn.setElevation(0f);
        btn.setMinimumHeight(0);
        btn.setPadding(dpToPx(2), dpToPx(6), dpToPx(2), dpToPx(6));
    }

    private void showCustomTipDialog()
    {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow() != null)
        {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE |
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        LinearLayout root = new LinearLayout(getContext());
        root.setOrientation(LinearLayout.VERTICAL);
        int pad = dpToPx(20);
        root.setPadding(pad, pad, pad, pad);
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(t.getBackgroundSecColor());
        bg.setCornerRadius(dpToPx(12));
        root.setBackground(bg);
        root.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView title = new TextView(getContext());
        title.setText("Custom Tip");
        title.setTextSize(profile.getTitleFontSize());
        title.setTextColor(t.getAccentColor());
        title.setTypeface(t.getFont());
        title.setGravity(Gravity.CENTER);
        root.addView(title);

        EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Enter tip %");
        input.setHintTextColor(t.getTextColor());
        input.setTextColor(t.getTextColor());
        input.setTextSize(profile.getTextFontSize());
        input.setTypeface(t.getFont());
        input.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams inputParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int spacing = dpToPx(12);
        inputParams.setMargins(0, spacing, 0, spacing);
        input.setLayoutParams(inputParams);
        root.addView(input);

        LinearLayout btnRow = new LinearLayout(getContext());
        btnRow.setOrientation(LinearLayout.HORIZONTAL);
        btnRow.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        Button cancelBtn = new Button(getContext());
        cancelBtn.setText("Cancel");
        cancelBtn.setAllCaps(false);
        cancelBtn.setTypeface(t.getFont());
        cancelBtn.setTextSize(profile.getTextFontSize());
        applyDialogBtnStyle(cancelBtn, false);
        LinearLayout.LayoutParams cancelParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        cancelParams.setMargins(0, 0, dpToPx(4), 0);
        cancelBtn.setLayoutParams(cancelParams);
        cancelBtn.setOnClickListener(v -> dialog.dismiss());
        btnRow.addView(cancelBtn);

        Button applyBtn = new Button(getContext());
        applyBtn.setText("Apply");
        applyBtn.setAllCaps(false);
        applyBtn.setTypeface(t.getFont());
        applyBtn.setTextSize(profile.getTextFontSize());
        applyDialogBtnStyle(applyBtn, true);
        LinearLayout.LayoutParams applyParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        applyParams.setMargins(dpToPx(4), 0, 0, 0);
        applyBtn.setLayoutParams(applyParams);
        applyBtn.setOnClickListener(v -> {
            String text = input.getText().toString().trim();
            if (!text.isEmpty())
            {
                try
                {
                    int tip = Integer.parseInt(text);
                    if (tip >= 0 && tip <= 100)
                    {
                        selectTip(tip);
                        dialog.dismiss();
                    }
                }
                catch (NumberFormatException ignored) {}
            }
        });
        btnRow.addView(applyBtn);
        root.addView(btnRow);

        dialog.setContentView(root);

        dialog.setOnShowListener(d -> {
            input.requestFocus();
            InputMethodManager imm = (InputMethodManager)
                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.showSoftInput(input, InputMethodManager.SHOW_FORCED);
        });

        dialog.show();

        if (dialog.getWindow() != null)
        {
            dialog.getWindow().setLayout(
                    (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.85f),
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }

    private void applyDialogBtnStyle(Button btn, boolean selected)
    {
        GradientDrawable btnBg = new GradientDrawable();
        btnBg.setCornerRadius(dpToPx(8));
        btnBg.setColor(selected ? t.getAccentColor() : t.getBackgroundSecColor());
        btn.setBackground(btnBg);
        btn.setTextColor(selected ? t.getBackgroundColor() : t.getTextColor());
        btn.setStateListAnimator(null);
        btn.setElevation(0f);
        btn.setMinimumHeight(0);
        btn.setPadding(dpToPx(2), dpToPx(6), dpToPx(2), dpToPx(6));
    }

    private void notifyObservers(int tip)
    {
        for (SliderObserver obs : observers) obs.handleSliderChange(tip, "tip");
    }

    private int dpToPx(int dp)
    {
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
