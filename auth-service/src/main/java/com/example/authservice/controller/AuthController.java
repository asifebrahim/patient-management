package com.example.authservice.controller;

import com.example.authservice.dto.LoginRequestDTO;
import com.example.authservice.dto.LoginResponseDTO;
import com.example.authservice.service.AuthService;
import com.example.authservice.service.userService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4005")
@RestController
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService, userService userervice) {
        this.authService = authService;

    }

    @Operation(summary = "Generate token on User Login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {

        // Attempt to authenticate the user and obtain the token

        Optional<String> tokenOptional = authService.authenticate(loginRequestDTO);

        // If the token is not present, return Unauthorized (401)
        if (tokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Otherwise, return the token wrapped in a response DTO
        String token = tokenOptional.get();
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @Operation(summary = "Validate Token")
    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Authorization header");
        }

        String token = authHeader.substring(7);
        boolean valid = authService.validateToken(token);

        return valid ?
                ResponseEntity.ok("Token is valid") :
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
    }


}
