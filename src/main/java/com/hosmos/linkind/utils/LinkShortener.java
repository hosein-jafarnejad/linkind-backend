package com.hosmos.linkind.utils;

import java.util.Random;

public class LinkShortener {
    private static String[] raw_data = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

    public static String makeShort() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt((raw_data.length));
            builder.append(raw_data[randomIndex]);
        }

        return builder.toString();
    }
}
