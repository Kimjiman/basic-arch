package com.basicarch.base.utils;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static boolean isEmpty(CharSequence val) {
        return val == null || val.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence val) {
        return !isEmpty(val);
    }

    public static CharSequence ifEmpty(CharSequence val, CharSequence replacedVal) {
        return isEmpty(val) ? replacedVal : val;
    }

    public static <T> void ifEmpty(CharSequence val, Consumer<CharSequence> action) {
        if (isEmpty(val)) action.accept(val);
    }

    public static <T> void ifNotEmpty(CharSequence val, Consumer<CharSequence> action) {
        if (isNotEmpty(val)) action.accept(val);
    }

    public static boolean isBlank(CharSequence val) {
        if (val == null || val.length() == 0) return true;
        for (int i = 0; i < val.length(); i++) {
            if(!Character.isWhitespace(val.charAt(i))) return false;
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence val) {
        return !isBlank(val);
    }

    public static CharSequence ifBlank(CharSequence val, CharSequence replacedVal) {
        return isBlank(val) ? replacedVal : val;
    }

    public static <T> void ifBlank(CharSequence val, Consumer<CharSequence> action) {
        if (isBlank(val)) action.accept(val);
    }

    public static <T> void ifNotBlank(CharSequence val, Consumer<CharSequence> action) {
        if (isNotBlank(val)) action.accept(val);
    }

    public static String removeWhiteSpace(String val) {
        return isEmpty(val) ? val : val.replaceAll("\\s", "");
    }

    /**
     * 문자(\p{L}), 숫자(p{N}), 빈칸(\s)을 제외한 모든 특수문자를 제거한다.
     *
     * @param val 입력 문자열
     * @return 문자, 숫자, 빈칸
     */
    public static String removeSpecialCharacters(String val) {
        return isEmpty(val) ? val : val.replaceAll("[^\\p{L}\\p{N}\\s]", "");
    }

    public static String remove(String val, Character charVal) {
        if (isEmpty(val) || charVal == null) return val;
        StringBuilder sb = new StringBuilder(val.length());
        for (int i = 0; i < val.length(); i++) {
            char currentChar = val.charAt(i);
            if (val.charAt(i) != charVal) {
                sb.append(currentChar);
            }
        }
        return sb.toString();
    }

    public static String upperCaseFirstCharacter(String val) {
        return isEmpty(val) ? val : Character.toUpperCase(val.charAt(0)) + val.substring(1);
    }

    public static boolean matches(String val, String regex) {
        if (isEmpty(val) || isEmpty(regex)) return false;
        return Pattern.matches(regex, val);
    }

    public static String matchingRegex(String val, String regex) {
        if (isEmpty(val) || isEmpty(regex)) return val;
        Matcher matcher = Pattern.compile(regex).matcher(val);
        return matcher.find() ? matcher.group() : val;
    }

    public static boolean isRegex(String val, String regex) {
        if (isEmpty(val) || isEmpty(regex)) return false;
        return Pattern.compile(regex).matcher(val).matches();
    }


    public static boolean isNumber(String val) {
        if (isEmpty(val)) return false;
        return isNotEmpty(val) && isRegex(val, "\\d+");
    }

    public static int indexOf(String val, char target) {
        return isEmpty(val) ? -1 : val.indexOf(target);
    }

    public static String reverse(String val) {
        return isEmpty(val) ? val : new StringBuilder(val).reverse().toString();
    }

    public static String masking(String val, int start, int length, CharSequence maskingCharacter) {
        if (isEmpty(val) || length <= 0) return val;
        StringBuilder sb = new StringBuilder(val.length());
        int min = Math.min(start + length, val.length());
        for (int i = start; i < min; i++) {
            sb.append(maskingCharacter);
        }
        return sb.insert(start, val, start, min).toString();
    }

    public static String masking(String val, int start, int length) {
        return masking(val, start, length, "*");
    }


    public static String joinStrings(Collection<CharSequence> val, CharSequence delimiter) {
        return val == null ? "" : String.join(delimiter, val);
    }

    public static String lpad(CharSequence val, int count, CharSequence repeatVal) {
        int repeatCount = Math.max(0, count - val.length());
        StringBuilder sb = new StringBuilder(val.length() + repeatCount * repeatVal.length());
        for (int i = 0; i < repeatCount; i++) {
            sb.append(repeatVal);
        }
        sb.append(val);
        return sb.toString();
    }

    public static String rpad(CharSequence val, int count, CharSequence repeatVal) {
        int repeatCount = Math.max(0, count - val.length());
        StringBuilder sb = new StringBuilder(val.length() + repeatCount * repeatVal.length());
        sb.append(val);
        for (int i = 0; i < repeatCount; i++) {
            sb.append(repeatVal);
        }
        return sb.toString();
    }

    public static String formatCurrency(String val) {
        Double amount;
        try {
            amount = Double.parseDouble(val);
            DecimalFormat formatter = new DecimalFormat("#,###");
            return formatter.format(amount);
        } catch (Exception e) {
            return val;
        }
    }
}
