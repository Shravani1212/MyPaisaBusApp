package com.mypaisa.bus.config;

import com.mypaisa.bus.model.User;
import com.mypaisa.bus.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .password("admin123")
                        .email("admin@gobus.com")
                        .build();
                userRepository.save(admin);
                System.out.println("SEED: Created default admin user.");
            }
        };
    }
}
