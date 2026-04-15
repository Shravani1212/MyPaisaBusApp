package com.mypaisa.bus.service;

import com.mypaisa.bus.model.User;
import com.mypaisa.bus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        // In a real app, use BCrypt. Simulating for demo.
        return userRepository.save(user);
    }

    public User login(String username, String password) {
        // Dummy login fallback for local/demo use without database access
        if ("admin".equals(username) && "admin123".equals(password)) {
            return User.builder()
                    .username(username)
                    .password(password)
                    .email("admin@gobus.com")
                    .build();
        }

        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password))
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
    }
}
