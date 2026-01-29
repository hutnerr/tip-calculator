package com.hbtipcalc.tipcalculator.math;

import com.hbtipcalc.tipcalculator.models.RoundingFlag;
import com.hbtipcalc.tipcalculator.models.TipResult;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Static class for the math behind the calculations.
 */
public class TipCalculator
{
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
     * @param roundingFlag to round after calculating the tip? NONE, UP, DOWN, or DYNAMIC
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
        BigDecimal total = inAmount.add(tipAmount);
        BigDecimal roundedTotal = applyRounding(total, roundingFlag);

        // ensure rounded total is never less than the original bill
        if (roundedTotal.compareTo(inAmount) < 0)
        {
            roundedTotal = inAmount;
        }

        BigDecimal adjustedTip = roundedTotal.subtract(inAmount);
        return new TipResult(adjustedTip, roundedTotal);
    }

    /**
     * Overloaded calculate method which takes in a String instead of a BigDecimal.
     * See above for full spec.
     */
    public static TipResult calculate(String inAmount, int tip, RoundingFlag roundingFlag)
    {
        return calculate(new BigDecimal(inAmount), tip, roundingFlag);
    }

    /**
     * Helper to calculate the actual tip.
     *
     * @param inAmount The amount initial Bill amount
     * @param tip The percentage. I.e. 15
     * @return The tip for this bill amount and tip percentage.
     */
    private static BigDecimal calculateTip(BigDecimal inAmount, int tip)
    {
        BigDecimal tipPercent = new BigDecimal(tip).divide(DIVISOR, 4, RoundingMode.HALF_UP);
        BigDecimal tipAmount = inAmount.multiply(tipPercent).setScale(2, RoundingMode.HALF_UP);
        return tipAmount;
    }

    /**
     * Rounds the total based on a specific flag. Can either not round, round up, round down, or dynamically round.
     *
     * @param total The total of the bill.
     * @param roundingFlag The flag. NONE, UP, DOWN, or DYNAMIC
     * @return The new total after it has been rounded.
     */
    private static BigDecimal applyRounding(BigDecimal total, RoundingFlag roundingFlag)
    {
        switch (roundingFlag)
        {
            case NONE:
                return total;
            case UP:
                return total.setScale(0, RoundingMode.CEILING);
            case DOWN:
                return total.setScale(0, RoundingMode.FLOOR);
            case DYNAMIC:
                // rounds to closest int
                return total.setScale(0, RoundingMode.HALF_UP);
            default:
                return total;
        }
    }
}