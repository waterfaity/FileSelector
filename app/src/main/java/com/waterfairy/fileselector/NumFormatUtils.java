package com.waterfairy.fileselector;

import java.math.RoundingMode;
import java.text.NumberFormat;

public class NumFormatUtils {

    public static String getRoundingNum(double value, int num) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(num);
        numberFormat.setRoundingMode(RoundingMode.UP);
        return numberFormat.format(value);
    }
}
