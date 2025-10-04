package com.hbtipcalc.tipcalculator.math;

import com.hbtipcalc.tipcalculator.models.RoundingFlag;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Static class for the math behind the calculations.
 */
public class TipCalculator {

    private static final BigDecimal DIVISOR = new BigDecimal("100");

    /**
     * Static helper class. We don't need to instantiate.
     */
    private TipCalculator() {}

    /**
     * Calculates an outcome using a dollar amount and a tip percentage.
     *
     * @param inAmount The dollar amount
     * @param tip The tip percentage (e.g. 20 for 20%)
     * @param roundingFlag to round after calculating the tip? RoundFlag.None, Up, Down
     * @return TipResult containing the tip amount and the total amount
     */
    public static TipResult calculate(BigDecimal inAmount, int tip, RoundingFlag roundingFlag)
    {
        if (inAmount.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new IllegalArgumentException("Negative In Amount");
        }

        if (tip < 0)
        {
            throw new IllegalArgumentException("Negative Tip");
        }

        BigDecimal tipAmount = calculateTip(inAmount, tip);
        BigDecimal total = calculateTotal(inAmount, tipAmount, roundingFlag);
        return new TipResult(tipAmount, total);
    }

    public static TipResult calculate(String inAmount, int tip, RoundingFlag roundingFlag)
    {
        return calculate(new BigDecimal(inAmount), tip, roundingFlag);
    }

    private static BigDecimal calculateTip(BigDecimal inAmount, int tip)
    {
        BigDecimal tipPercent = new BigDecimal(tip).divide(DIVISOR, 4, RoundingMode.HALF_UP);
        BigDecimal tipAmount = inAmount.multiply(tipPercent).setScale(2, RoundingMode.HALF_UP);
        return tipAmount;
    }

    private static BigDecimal calculateTotal(BigDecimal inAmount, BigDecimal tipAmount, RoundingFlag roundingFlag)
    {
        BigDecimal total = inAmount.add(tipAmount);

        switch (roundingFlag)
        {
            case NONE:
                break;
            case UP:
                total = total.setScale(0, RoundingMode.CEILING);
                break;
            case DOWN:
                total = total.setScale(0, RoundingMode.FLOOR);
                break;
            case DYNAMIC:;
                // TODO: FILL THIS OUT
                break;
        }
        return total;
    }
}
