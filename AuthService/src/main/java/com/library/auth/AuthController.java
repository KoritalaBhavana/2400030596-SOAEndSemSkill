
package com.library.auth;

import java.util.Map;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public Map<String, String> login(
            @RequestBody Map<String, String> data) {

        String username = data.get("username");
        String password = data.get("password");

        if ("admin".equals(username)
                && "admin123".equals(password)) {

            String token =
                jwtService.generateToken(username);

            return Map.of("token", token);
        }

        throw new ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "Invalid username or password");
    }

    @GetMapping("/validate")
    public String validate(
            @RequestParam String token) {

        if (jwtService.validateToken(token)) {
            return "Token is valid";
        }

        throw new ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "Invalid JWT token");
    }
}
