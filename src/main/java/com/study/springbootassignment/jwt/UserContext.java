package com.study.springbootassignment.jwt;

import com.study.springbootassignment.entity.UserEntity;

public class UserContext {
    private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        userIdHolder.set(userId);
    }

    public static Long getUserId() {
        return userIdHolder.get();
    }

    public static void clear() {
        userIdHolder.remove();
    }
}
