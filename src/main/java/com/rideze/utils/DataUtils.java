package com.rideze.utils;

import java.util.Random;

public class DataUtils {

    private static final Random RANDOM = new Random();

    public static String generateUniqueRegistrationNumber() {
        int randomDigits = 1000 + RANDOM.nextInt(9000);
        return "KA01AB" + randomDigits;
    }

    public static String generateUniqueInvalidRegistrationNumber() {
        int randomDigits = 1000 + RANDOM.nextInt(9000);
        return "INV!" + randomDigits;
    }
}