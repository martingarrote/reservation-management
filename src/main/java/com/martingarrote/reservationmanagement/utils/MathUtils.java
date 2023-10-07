package com.martingarrote.reservationmanagement.utils;

public class MathUtils {

    public double calculateValueWithDiscount(double bruteValue, double discountPercentage) {
        double realDiscount = bruteValue * discountPercentage / 100;

        return bruteValue - realDiscount;
    }
}
