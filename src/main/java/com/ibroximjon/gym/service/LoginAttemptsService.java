package com.ibroximjon.gym.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptsService {

    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final long BLOCK_DURATION_MINUTES = 5;

    private final ConcurrentHashMap<String, Integer> attempts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalDateTime> blockedUntil = new ConcurrentHashMap<>();

    public void loginSucceeded(String username) {
        attempts.remove(username);
        blockedUntil.remove(username);
    }

    public void loginFailed(String username) {
        attempts.put(username, attempts.getOrDefault(username, 0) + 1);
        if (attempts.get(username) >= MAX_LOGIN_ATTEMPTS) {
            blockedUntil.put(username, LocalDateTime.now().plusMinutes(BLOCK_DURATION_MINUTES));
        }
    }

    public boolean isBlocked(String username) {
        if (!blockedUntil.containsKey(username)) return false;
        if (LocalDateTime.now().isAfter(blockedUntil.get(username))) {
            blockedUntil.remove(username);
            attempts.remove(username);
            return false;
        }
        return true;
    }

    public long minutesLeft(String username) {
        if (!isBlocked(username)) return 0;
        return java.time.Duration.between(LocalDateTime.now(), blockedUntil.get(username)).toMinutes();
    }
}
