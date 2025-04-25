package com.example.authservice.service;

import com.example.authservice.model.User;
import com.example.authservice.repository.userRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class userService {
    private final userRepository userrepository;
    public userService(userRepository userrepository) {
        this.userrepository = userrepository;
    }

    @PostConstruct
    public void testFindUser() {
        String testEmail = "asifebrahim13@gmail.com";
        Optional<User> user = userrepository.findByEmail(testEmail);
        System.out.println(user.isPresent() ? "User found: " + user.get() : "User NOT found");
    }


    public Optional<User> findByEmail(String email) {
        email = email.trim();  // Ensure there are no spaces around the email
        Optional<User> userOpt = userrepository.findByEmail(email);
        String finalEmail = email;
        userOpt.ifPresentOrElse(user -> System.out.println("User found: " + user.getEmail()),
                () -> System.out.println("User not found with email: " + finalEmail));
        return userOpt;
    }


}
