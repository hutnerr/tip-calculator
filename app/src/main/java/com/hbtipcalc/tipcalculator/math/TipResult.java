package com.hbtipcalc.tipcalculator.math;

import java.math.BigDecimal;

public class TipResult {

    private BigDecimal tip;
    private BigDecimal total;

    public TipResult(BigDecimal tip, BigDecimal total)
    {
        this.tip = tip;
        this.total = total;
    }

    public BigDecimal getTip()
    {
        return this.tip;
    }

    public BigDecimal getTotal()
    {
        return this.total;
    }

    public String getFormattedTip()
    {
        return String.format("$%.2f", tip);
    }

    public String getFormattedTotal()
    {
        return String.format("$%.2f", total);
    }

    public String getFormatted()
    {
        return getFormattedTip() + " " + getFormattedTotal();
    }
}
