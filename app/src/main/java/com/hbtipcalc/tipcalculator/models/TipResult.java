package com.hbtipcalc.tipcalculator.models;

import java.math.BigDecimal;

/**
 * Contains the result of a Tip Calculation.
 */
public class TipResult
{
    private static String currencySymbol = "$";

    private final BigDecimal tip;   // The amount of the tip
    private final BigDecimal total; // The total amount (bill + tip)
    private final BigDecimal split; // The amount per person after being split

    /**
     * Constructor.
     *
     * @param tip The dollar amount of the tip.
     * @param total The dollar amount of the total.
     */
    public TipResult(BigDecimal tip, BigDecimal total, BigDecimal split)
    {
        this.tip = tip;
        this.total = total;
        this.split = split;
    }

    // Getters for instance variables
    public BigDecimal getTip()
    {
        return this.tip;
    }
    public BigDecimal getTotal()
    {
        return this.total;
    }
    public BigDecimal getSplit()
    {
        return this.split;
    }
    public BigDecimal getBill() { return this.total.subtract(this.tip); }

    // Getters for formatted versions of instance variables
    private String formatBD(BigDecimal val)
    {
        return String.format("%s%.2f", currencySymbol, val);
    }
    public String getFormattedTip() { return formatBD(tip); }
    public String getFormattedSplit()
    {
        return formatBD(split);
    }
    public String getFormattedTotal()
    {
        return formatBD(total);
    }
    public String getFormattedBill() { return formatBD(getBill()); }

    // Sets the currency symbol so it gets formatted properly.
    // It is static so it only has to be set on changes
    public static void setCurrencySymbol(String symbol) {
        if (symbol == null || symbol.trim().isEmpty())
        {
            currencySymbol = "";
        }
        else
        {
            currencySymbol = symbol;
        }
    }


    // below should be called to return instead of erroring out
    public static TipResult getInvalidTipResult()
    {
        return new TipResult(new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"));
    }
}
