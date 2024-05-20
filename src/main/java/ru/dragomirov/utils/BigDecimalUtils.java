package ru.dragomirov.utils;

import java.math.BigDecimal;

public class BigDecimalUtils {
    public static BigDecimal parseBigDecimal(String string) {
        BigDecimal rate = null;
        try {
            rate = new BigDecimal(string);
        } catch (NumberFormatException e) {
            System.err.println("Произошла ошибка при выполнении метода 'parseBigDecimal': " + e.getMessage());
            e.printStackTrace();
        }
        return rate;
    }
}
