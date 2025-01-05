package com.app.manage_money.utils;

import java.math.BigDecimal;

public class BigDecimalUtils {
    public static boolean isGreaterThan(BigDecimal x, BigDecimal y) {
        return x.compareTo(y) > 0;
    }

    public static boolean isLessThan(BigDecimal x, BigDecimal y) {
        return x.compareTo(y) < 0;
    }

    public static boolean isEqual(BigDecimal x, BigDecimal y) {
        return x.compareTo(y) == 0;
    }

    public static boolean isGreaterThanOrEqual(BigDecimal x, BigDecimal y) {
        return x.compareTo(y) >= 0;
    }

    public static boolean isLessThanOrEqual(BigDecimal x, BigDecimal y) {
        return x.compareTo(y) <= 0;
    }
}
