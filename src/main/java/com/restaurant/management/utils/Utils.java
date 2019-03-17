package com.restaurant.management.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class Utils {
    private final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String ALPHABET2 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";


    private String generateRandomStringWithSmallLetters(int length) {
        StringBuilder returnValue = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }

    private String generateRandomStringWithBigLetters(int length) {
        StringBuilder returnValue = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHABET2.charAt(RANDOM.nextInt(ALPHABET2.length())));
        }
        return new String(returnValue);
    }

    public String generateProductUniqueId(int length) {
        return generateRandomStringWithBigLetters(length);
    }

    public String generateCartUniqueId(int length) {
        return generateRandomStringWithBigLetters(length);
    }

    public String generateOrderNumber(int length) {
        return generateRandomStringWithBigLetters(length);
    }

    public String generateUserUniqueId(int length) {
        return generateRandomStringWithSmallLetters(length);
    }

    public String generateDailyOrderListUniqueId(int length) {
        return generateRandomStringWithBigLetters(length);
    }
}