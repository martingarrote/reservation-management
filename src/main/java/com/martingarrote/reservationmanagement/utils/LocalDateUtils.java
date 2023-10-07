package com.martingarrote.reservationmanagement.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateUtils {

    public static LocalDate stringToLocalDate(String dateString) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(dateString, dateFormat);

        return date;
    }

    public static String formatLocalDate(LocalDate date) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        date.format(dateFormat);

        return date.toString();
    }

    public static LocalDate getFutureDateBasedInMonthsQuantity(int months) {
        return LocalDate.now().plusMonths(months);
    }

    public static int getAgeByDateOfBirth(LocalDate dateOfBirth) {
        return dateOfBirth.until(LocalDate.now()).getYears();
    }

}
