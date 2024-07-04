package com.shortUrl.url.shortner;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;

public class UniqueHash {

    public static String generateUniqueHash(String input) throws NoSuchAlgorithmException {
        input += Instant.now().toString();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));

        // Convert byte array to hex string
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros if necessary
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }

        return hexString.substring(0,30);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {

        LocalDateTime t1 = LocalDateTime.now().plusDays(10L);
        LocalDateTime t2 = LocalDateTime.now();

        System.out.println(t1.toLocalDate().equals(t2.toLocalDate()));

//        String str = "Hello World";
//        String hash = generateUniqueHash(str);
//        System.out.println(hash);
//        String str2 = "World Hello";
//        String hash2 = generateUniqueHash(str2);
//        System.out.println(hash2);
    }
}