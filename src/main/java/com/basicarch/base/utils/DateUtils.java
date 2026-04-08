package com.basicarch.base.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String NULL_STRING = "";

    public static boolean isValidDateFormat(String dateString, String pattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            formatter.parse(dateString);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static Date localDateTimeToDate(LocalDateTime date) {
        if(date == null) return null;
        return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        if(date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDate dateToLocalDate(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime stringToLocalDateTime(String dateString, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateString, formatter);
    }

    public static LocalDate stringToLocalDate(String dateString, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(dateString, formatter);
    }

    public static Date stringToDate(String dateString, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String localDateTimeToString(LocalDateTime date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String localDateToString(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String dateToString(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public static String convertToPattern(String dateString, String pattern, String convertPattern) {
        if (!isValidDateFormat(dateString, pattern)) return dateString;
        LocalDateTime localDateTime = stringToLocalDateTime(dateString, pattern);
        return localDateTimeToString(localDateTime, convertPattern);
    }

    public static String convertToPattern(String dateString, String pattern) {
        return convertToPattern(dateString, pattern, DATETIME_PATTERN);
    }

    public static String getDateTimeString(LocalDateTime date) {
        if(date == null) return NULL_STRING;
        return localDateTimeToString(date, DATETIME_PATTERN);
    }

    public static String getDateString(LocalDateTime date) {
        if(date == null) return NULL_STRING;
        return localDateTimeToString(date, DATE_PATTERN);
    }

    public static String getTimeString(LocalDateTime date) {
        if(date == null) return NULL_STRING;
        return localDateTimeToString(date, TIME_PATTERN);
    }

    public static Integer getDayOfWeekNumber(LocalDateTime date) {
        if(date == null) return null;
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek.getValue();
    }

    public static String getDayOfWeekKor(LocalDateTime date) {
        if(date == null) return NULL_STRING;
        return DayOfWeeks.getKor(getDayOfWeekNumber(date));
    }

    public static String getDayOfWeekKorShort(LocalDateTime date) {
        if(date == null) return NULL_STRING;
        return DayOfWeeks.getKorShort(getDayOfWeekNumber(date));
    }

    public static String getDayOfWeekEng(LocalDateTime date) {
        if(date == null) return NULL_STRING;
        return DayOfWeeks.getEng(getDayOfWeekNumber(date));
    }

    public static String getDayOfWeekEngShort(LocalDateTime date) {
        if(date == null) return NULL_STRING;
        return DayOfWeeks.getEngShort(getDayOfWeekNumber(date));
    }

    public static LocalDateTime millisToLocalDateTime(long milliseconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault());
    }

    public static String millisToString(long milliseconds, String pattern) {
        LocalDateTime localDateTime = millisToLocalDateTime(milliseconds);
        return localDateTimeToString(localDateTime, pattern);
    }

    public static int compareTo(LocalDateTime date, LocalDateTime date2) {
        return date.compareTo(date2);
    }

    public static long daysBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    public static boolean isBetween(LocalDateTime date, LocalDateTime date2, LocalDateTime targetDate) {
        LocalDateTime startDate = (date.isBefore(date2)) ? date : date2;
        LocalDateTime endDate = (date.isAfter(date2)) ? date : date2;

        return (targetDate.equals(startDate) || targetDate.isAfter(startDate)) &&
                (targetDate.equals(endDate) || targetDate.isBefore(endDate));
    }

    public static String getDateTimeString(Date date) {
        if(date == null) return NULL_STRING;
        return dateToString(date, DATETIME_PATTERN);
    }

    public static String getDateString(Date date) {
        if(date == null) return NULL_STRING;
        return dateToString(date, DATE_PATTERN);
    }

    public static String getTimeString(Date date) {
        if(date == null) return NULL_STRING;
        return dateToString(date, TIME_PATTERN);
    }

    public static Date millisToDate(long millis) {
        return new Date(millis);
    }

    public static Integer getDayOfWeekNumber(Date date) {
        LocalDateTime date2 = dateToLocalDateTime(date);
        return getDayOfWeekNumber(date2);
    }

    public static String getDayOfWeekKor(Date date) {
        if(date == null) return NULL_STRING;
        LocalDateTime date2 = dateToLocalDateTime(date);
        return getDayOfWeekKor(date2);
    }

    public static String getDayOfWeekKorShort(Date date) {
        if(date == null) return NULL_STRING;
        LocalDateTime date2 = dateToLocalDateTime(date);
        return getDayOfWeekKorShort(date2);
    }

    public static String getDayOfWeekEng(Date date) {
        if(date == null) return NULL_STRING;
        LocalDateTime date2 = dateToLocalDateTime(date);
        return getDayOfWeekEng(date2);
    }

    public static String getDayOfWeekEngShort(Date date) {
        if(date == null) return NULL_STRING;
        LocalDateTime date2 = dateToLocalDateTime(date);
        return getDayOfWeekEngShort(date2);
    }

    public static int compareTo(Date date, Date date2) {
        return date.compareTo(date2);
    }

    public static long daysBetween(Date startDate, Date endDate) {
        LocalDateTime start = dateToLocalDateTime(startDate);
        LocalDateTime end = dateToLocalDateTime(endDate);
        return daysBetween(start, end);
    }

    public static boolean isBetween(Date date, Date date2, Date targetDate) {
        Date startDate = (date.before(date2)) ? date : date2;
        Date endDate = (date.after(date2)) ? date : date2;

        return (targetDate.equals(startDate) || targetDate.after(startDate)) &&
                (targetDate.equals(endDate) || targetDate.before(endDate));
    }

    public static Date manipulateDate(Date date, int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        date = calendar.getTime();
        return date;
    }

    public static Date plusYears(Date date, int year) {
        return manipulateDate(date, Calendar.YEAR, year);
    }

    public static Date minusYears(Date date, int year) {
        return manipulateDate(date, Calendar.YEAR, -year);
    }

    public static Date plusMonths(Date date, int month) {
        return manipulateDate(date, Calendar.MONTH, month);
    }

    public static Date minusMonths(Date date, int month) {
        return manipulateDate(date, Calendar.MONTH, -month);
    }

    public static Date plusDays(Date date, int day) {
        return manipulateDate(date, Calendar.DATE, day);
    }

    public static Date minusDays(Date date, int day) {
        return manipulateDate(date, Calendar.DATE, -day);
    }

    public static Date plusHours(Date date, int hour) {
        return manipulateDate(date, Calendar.HOUR, hour);
    }

    public static Date minusHours(Date date, int hour) {
        return manipulateDate(date, Calendar.HOUR, -hour);
    }

    public static Date plusMinutes(Date date, int minute) {
        return manipulateDate(date, Calendar.MINUTE, minute);
    }

    public static Date minusMinutes(Date date, int minute) {
        return manipulateDate(date, Calendar.MINUTE, -minute);
    }

    public static Date plusSeconds(Date date, int second) {
        return manipulateDate(date, Calendar.SECOND, second);
    }

    public static Date minusSeconds(Date date, int second) {
        return manipulateDate(date, Calendar.SECOND, -second);
    }
}

enum DayOfWeeks {
    MONDAY(1, "월요일", "월", "Monday", "Mon."),
    TUESDAY(2, "화요일", "화", "Tuesday", "Tue."),
    WEDNESDAY(3, "수요일", "수", "Wednesday", "Wed."),
    THURSDAY(4, "목요일", "목", "Thursday", "Thu."),
    FRIDAY(5, "금요일", "금", "Friday", "Fri."),
    SATURDAY(6, "토요일", "토", "Saturday", "Sat."),
    SUNDAY(7, "일요일", "일", "Sunday", "Sun.");

    private final int dayOfWeekNumber;
    private final String kor;
    private final String korShort;
    private final String eng;
    private final String engShort;

    DayOfWeeks(int dayOfWeekNumber, String kor, String korShort, String eng, String engShort) {
        this.dayOfWeekNumber = dayOfWeekNumber;
        this.kor = kor;
        this.korShort = korShort;
        this.eng = eng;
        this.engShort = engShort;
    }

    public int getDayOfWeekNumber() {
        return dayOfWeekNumber;
    }

    public String getKor() {
        return kor;
    }

    public String getKorShort() {
        return korShort;
    }

    public String getEng() {
        return eng;
    }

    public String getEngShort() {
        return engShort;
    }

    public static String getKor(int dayOfWeekNumber) {
        return Arrays.stream(values())
                .filter(dayOfweek -> dayOfweek.dayOfWeekNumber == dayOfWeekNumber)
                .findFirst()
                .map(DayOfWeeks::getKor)
                .orElseThrow();
    }

    public static String getKorShort(int dayOfWeekNumber) {
        return Arrays.stream(values())
                .filter(dayOfweek -> dayOfweek.dayOfWeekNumber == dayOfWeekNumber)
                .findFirst()
                .map(DayOfWeeks::getKorShort)
                .orElseThrow();
    }

    public static String getEng(int dayOfWeekNumber) {
        return Arrays.stream(values())
                .filter(dayOfweek -> dayOfweek.dayOfWeekNumber == dayOfWeekNumber)
                .findFirst()
                .map(DayOfWeeks::getEng)
                .orElseThrow();
    }

    public static String getEngShort(int dayOfWeekNumber) {
        return Arrays.stream(values())
                .filter(dayOfweek -> dayOfweek.dayOfWeekNumber == dayOfWeekNumber)
                .findFirst()
                .map(DayOfWeeks::getEngShort)
                .orElseThrow();
    }
}