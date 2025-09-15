package com.study.springbootassignment.util;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class AccountHelper {
    public String randomAccountNumber(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be positive");
        }

        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder(length);

        // First digit: 1-9
        sb.append(random.nextInt(1, 10));

        // Remaining digits: 0-9
        for (int i = 1; i < length; i++) {
            sb.append(random.nextInt(0, 10));
        }

        return sb.toString();
    }
}
