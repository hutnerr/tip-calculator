package com.hbtipcalc.tipcalculator.models;

import java.math.BigDecimal;

/**
 * Contains the result of a Tip Calculation.
 */
public class TipResult
{
    private static String currencySymbol = "$";

    private final BigDecimal tip;
    private final BigDecimal total;

    /**
     * Constructor.
     *
     * @param tip The dollar amount of the tip.
     * @param total The dollar amount of the total.
     */
    public TipResult(BigDecimal tip, BigDecimal total)
    {
        this.tip = tip;
        this.total = total;
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
    public BigDecimal getBill() { return this.total.subtract(this.tip); }

    // Getters for formatted versions of instance variables
    private String formatBD(BigDecimal val)
    {
        return String.format("%s%.2f", currencySymbol, val);
    }
    public String getFormattedTip()
    {
        return formatBD(tip);
    }
    public String getFormattedTotal()
    {
        return formatBD(total);
    }
    public String getFormattedBill() { return formatBD(getBill()); }


    public static void setCurrencySymbol(String symbol)
    {
        currencySymbol = symbol;
    }

}
