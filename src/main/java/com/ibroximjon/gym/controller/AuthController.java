package com.ibroximjon.gym.controller;

import com.ibroximjon.gym.dto.ChangePasswordRequestDTO;
import com.ibroximjon.gym.dto.LoginRequest;
import com.ibroximjon.gym.dto.RegisterRequestDTO;
import com.ibroximjon.gym.model.User;
import com.ibroximjon.gym.service.AuthService;
import com.ibroximjon.gym.service.LoginAttemptsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;
    private final LoginAttemptsService loginAttemptsService;

    public AuthController(AuthService authService, LoginAttemptsService loginAttemptsService) {
        this.authService = authService;
        this.loginAttemptsService = loginAttemptsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {

            String token = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok().body("Bearer " + token);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            // 1. Raw password ni saqlab qolamiz
            String rawPassword = registerRequestDTO.getPassword();

            // 2. Foydalanuvchini yaratamiz
            User user = new User();
            user.setUsername(registerRequestDTO.getUsername());
            user.setPassword(rawPassword); // bcrypt keyinroq bo'ladi
            user.setActive(true);
            user.setFirstName(registerRequestDTO.getFirstName());
            user.setLastName(registerRequestDTO.getLastName());

            // 3. Ro‘yxatdan o‘tkazamiz (bunda password encoder ishlaydi)
            authService.register(user);

            // 4. JWT olish uchun login qilamiz (raw password bilan)
            String token = authService.login(user.getUsername(), rawPassword);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User registered successfully. Token = " + token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error registering user: " + e.getMessage());
        }
    }


    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(Authentication authentication,@RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {

        String username = authentication.getName();

        if (loginAttemptsService.isBlocked(username)) {
            long minutesLeft = loginAttemptsService.minutesLeft(username);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("🚫 User is temporarily blocked. Try again in " + minutesLeft + " minute(s).");
        }

        boolean changed = authService.changePassword(username, changePasswordRequestDTO.getOldPassword(), changePasswordRequestDTO.getNewPassword());
        if (changed) {
            loginAttemptsService.loginSucceeded(username);
            return ResponseEntity.ok().body("Password changed.");
        }
        loginAttemptsService.loginFailed(username);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid old password.");
    }
}
