package com.martingarrote.reservationmanagement.utils;

import java.time.LocalDate;

public class LocalDateUtils {
    public static LocalDate getFutureDateBasedInMonthsQuantity(int months) {
        return LocalDate.now().plusMonths(months);
    }

    public static int getAgeByDateOfBirth(LocalDate dateOfBirth) {
        return dateOfBirth.until(LocalDate.now()).getYears();
    }

}
