package com.client.utils;

import java.sql.Date;
import java.util.Calendar;

/**
 * @author sdaskaliesku
 */
public class DateUtils {

    public static Date getCurrentDate() {
        return new Date(new java.util.Date().getTime());
    }

    public static boolean isDateBeforeToday(Date dateToCompare) {
        return getCurrentDate().before(dateToCompare);
    }

    public static boolean isDateAfterToday(Date dateToCompare) {
        return getCurrentDate().after(dateToCompare);
    }

    public static boolean isDateEqualToday(Date dateToCompare) {
        return getCurrentDate().equals(dateToCompare);
    }

    public static boolean isDateEqualOrBeforeToday(Date dateToCompare) {
        return isDateBeforeToday(dateToCompare) || isDateEqualToday(dateToCompare);
    }

    public static Date addDay(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return new Date(calendar.getTimeInMillis());
    }

    public static Date addMonth(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return new Date(calendar.getTimeInMillis());
    }

    public static Date addYear(Date date, int years) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, years);
        return new Date(calendar.getTimeInMillis());
    }

    public static Date addDay(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getCurrentDate());
        calendar.add(Calendar.DATE, days);
        return new Date(calendar.getTimeInMillis());
    }

    public static Date addMonth(int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getCurrentDate());
        calendar.add(Calendar.MONTH, months);
        return new Date(calendar.getTimeInMillis());
    }

    public static Date addYear(int years) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getCurrentDate());
        calendar.add(Calendar.YEAR, years);
        return new Date(calendar.getTimeInMillis());
    }
}
