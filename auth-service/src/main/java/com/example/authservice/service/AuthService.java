package com.example.authservice.service;

import com.example.authservice.dto.LoginRequestDTO;
import com.example.authservice.dto.LoginResponseDTO;
import com.example.authservice.model.User;
import com.example.authservice.util.jwtUtil;
import com.example.authservice.util.jwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final userService userservice;
    private final PasswordEncoder passwordEncoder;
    private final jwtUtil jwtutil;

    public AuthService(userService userservice, PasswordEncoder passwordEncoder, jwtUtil jwtutil) {
        this.userservice = userservice;
        this.passwordEncoder = passwordEncoder;
        this.jwtutil = jwtutil;
    }

    public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {
        Optional<User> userOptional = userservice.findByEmail(loginRequestDTO.getEmail());

        if (userOptional.isEmpty()) {
            System.out.println("User not found with email: " + loginRequestDTO.getEmail());  // Log missing user
            return Optional.empty();
        }

        User user = userOptional.get();
        boolean passwordMatches = passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword());

        if (!passwordMatches) {
            System.out.println("Password mismatch for user: " + loginRequestDTO.getEmail());  // Log failed password match
            return Optional.empty();
        }

        String token = jwtutil.generateToken(user.getEmail(), user.getRole());
        return Optional.of(token);
    }
    public boolean validateToken(String token) {
        try{
            jwtutil.validateToken(token);
            return true;
        }
        catch(JwtException e){
            return false;
        }
    }
}

