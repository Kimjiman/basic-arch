package com.basicarch.base.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtils {
    private static final int DECIMAL_LENGTH = 1;
    private static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * 부동소수점 계산
     * @param val 소수점 데이터
     * @param multiplier 곱할 변수
     * @param length 소수점 자리수
     * @return 계산된 값
     * RoundingMode.HALF_UP: 반올림. 소수 부분이 0.5 이상이면 올림, 0.5 미만이면 내림.
     * RoundingMode.HALF_DOWN: 반올림. 소수 부분이 0.5 이상이면 올림, 0.5 미만이면 내림.
     * RoundingMode.UP: 올림. 언제나 양수 방향으로 반올림.
     * RoundingMode.DOWN: 내림. 언제나 음수 방향으로 반올림.
     * RoundingMode.CEILING: 양의 무한대 방향으로 올림.
     * RoundingMode.FLOOR: 음의 무한대 방향으로 내림.
     * RoundingMode.HALF_EVEN: "짝수" 방식의 반올림. 소수 부분이 0.5일 경우, 짝수 자릿수에서 가장 가까운 짝수로 반올림.
     */
    public static <T extends Number> String multiplyAndRound(T val, T multiplier, int length, RoundingMode roundingMode) {
        BigDecimal originVal = new BigDecimal(val.toString());
        BigDecimal multipVal = originVal.multiply(new BigDecimal(multiplier.toString()));
        BigDecimal ret = multipVal.setScale(length, roundingMode);
        return ret.toString();
    }

    public static <T extends Number> String multiplyAndRound(T val, T multiplier, int length) {
        return multiplyAndRound(val, multiplier, length, DEFAULT_ROUNDING_MODE);
    }

    public static <T extends Number> String multiplyAndRound(T val, T multiplierVal) {
        return multiplyAndRound(val, multiplierVal, DECIMAL_LENGTH);
    }

    @SafeVarargs
    public static <T extends Number & Comparable<T>> T max(T ...vals) {
        if (vals == null || vals.length == 0) {
            throw new IllegalArgumentException("Input array must not be empty");
        }

        T maxVal = vals[0];
        for (int i = 1; i < vals.length; i++) {
            if (vals[i].compareTo(maxVal) > 0) {
                maxVal = vals[i];
            }
        }

        return maxVal;
    }

    @SafeVarargs
    public static <T extends Number & Comparable<T>> T min(T ...vals) {
        if (vals == null || vals.length == 0) {
            throw new IllegalArgumentException("Input array must not be empty");
        }

        T minVal = vals[0];
        for (int i = 1; i < vals.length; i++) {
            if (vals[i].compareTo(minVal) < 0) {
                minVal = vals[i];
            }
        }

        return minVal;
    }
}
