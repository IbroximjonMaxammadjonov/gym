package com.ibroximjon.gym.controller;

import com.ibroximjon.gym.dto.LoginRequest;
import com.ibroximjon.gym.dto.RegisterRequestDTO;
import com.ibroximjon.gym.model.User;
import com.ibroximjon.gym.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
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

        try{
            User user = registerRequestDTO.toUser();
            authService.register(user);
            String token = authService.login(user.getUsername(), user.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully: token = " + token);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error registering user");
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam String username, @RequestParam String password, @RequestParam String newPassword) {
        boolean changed = authService.changePassword(username, password, newPassword);
        if (changed) {
            return ResponseEntity.ok().body("Password changed.");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
    }
}
