package com.hbtipcalc.tipcalculator.controllers;

import com.hbtipcalc.tipcalculator.math.TipCalculator;
import com.hbtipcalc.tipcalculator.models.TipResult;
import com.hbtipcalc.tipcalculator.models.RoundingFlag;
import com.hbtipcalc.tipcalculator.view.elements.SliderObserver;
import com.hbtipcalc.tipcalculator.view.elements.TextBoxObserver;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This is the class that will receive updates from the view, perform calculations, then
 * send them back to the UI. It also stores the relevant data for the calculator.
 */
public class Calculator implements SliderObserver, TextBoxObserver {
    private static final String[] CURRENCIES = { "$", "€", "£", "₹", "₣", "R", "kr" };
    private static final Pattern CURRENCY_PREFIX = Pattern.compile("^\\s*(?:\\$|€|£|₹|₣|R|kr)\\s*");

    private final List<CalculatorObserver> observers;

    private BigDecimal billAmt;
    private int tipPercent;
    private RoundingFlag roundingFlag;
    private int splitCount;

    /**
     * Constructor.
     *
     * @param billAmt The BigDecimal of the bill that needs to be paid.
     * @param tipPercent The int percent that wants to be paid
     * @param roundingFlag UP, DOWN, NONE, DYNAMIC. Determines how rounding is handled. See RoundingFlag class for more info
     */
    public Calculator(BigDecimal billAmt, int tipPercent, RoundingFlag roundingFlag)
    {
        this.observers = new ArrayList<>();
        this.billAmt = billAmt;
        this.tipPercent = tipPercent;
        this.roundingFlag = roundingFlag;
        this.splitCount = 2;
    }

    public void setRoundingFlag(RoundingFlag flag)
    {
        this.roundingFlag = flag;
    }

    public void setSplitCount(int splitCount)
    {
        this.splitCount = splitCount;
    }

    /**
     * Uses the stored information to perform a calculation then sends it back out.
     */
    public void calculate()
    {
        System.out.println("DEBUG: billAmt=" + billAmt + ", tipPercent=" + tipPercent + ", roundingFlag=" + roundingFlag);
        TipResult result = TipCalculator.calculate(billAmt, tipPercent, roundingFlag, splitCount);
        System.out.println("DEBUG: result tip=" + result.getTip() + ", total=" + result.getTotal());
        notifyObservers(result);
    }

    /**
     * Adds an observer to be notified when a calculation is performed.
     *
     * @param observer The observer to add.
     */
    public void addObserver(CalculatorObserver observer)
    {
        this.observers.add(observer);
    }

    /**
     * Removes an observer.
     *
     * @param observer The observer to remove
     */
    public void removeObserver(CalculatorObserver observer)
    {
        this.observers.remove(observer);
    }

    /**
     * Notifies all observers of a new calculation.
     *
     * @param result
     */
    public void notifyObservers(TipResult result)
    {
        for (CalculatorObserver observer : observers)
        {
            observer.handleTipCalculation(result);
        }
    }

    /**
     * When the connected slider is changed, we want to grab its new value as it becomes our
     * new Tip Percent. Also calls calculate to send out the new calculation.
     *
     * @param newVal The new tip percent
     */
    @Override
    public void handleSliderChange(int newVal, String sliderID)
    {
        if ("tip".equals(sliderID))
        {
            this.tipPercent = newVal;
        }
        else if ("split".equals(sliderID))
        {
            this.splitCount = newVal;
        }
        calculate();
    }

    public void handleText(String newText)
    {
        if (newText == null || newText.isBlank()) {
            return;
        }

        String cleaned = CURRENCY_PREFIX
                .matcher(newText)
                .replaceFirst("")
                .replace(",", "")
                .trim();

        try {
            this.billAmt = new BigDecimal(cleaned);
            calculate();
        } catch (NumberFormatException e) {
        }
    }

    public BigDecimal getBillAmt()
    {
        return this.billAmt;
    }

    public int getSplitCount()
    {
        return this.splitCount;
    }
}
