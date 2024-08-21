package com.example.kgs;

public class Base62EncoderDecoder {

    private static final String BASE62_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = BASE62_ALPHABET.length();

    public static String encode(long value) {
        StringBuilder encoded = new StringBuilder();

        // 這裡我們直接使用 long 值進行處理，而不是轉換為 BigInteger
        while (value > 0) {
            int remainder = (int)(value % BASE);
            encoded.append(BASE62_ALPHABET.charAt(remainder));
            value /= BASE;
        }

        // 如果 value 是 0，返回字母 '0'
        if (encoded.length() == 0) {
            return "0";
        }

        return encoded.reverse().toString();
    }

    public static long decode(String input) {
        long value = 0;
        for (char c : input.toCharArray()) {
            value = value * BASE + BASE62_ALPHABET.indexOf(c);
        }
        return value;
    }
}



