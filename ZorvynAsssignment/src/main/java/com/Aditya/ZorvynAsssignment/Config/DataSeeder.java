package com.Aditya.ZorvynAsssignment.Config;

import com.Aditya.ZorvynAsssignment.Entity.Role;
import com.Aditya.ZorvynAsssignment.Entity.RoleName;
import com.Aditya.ZorvynAsssignment.Entity.User;
import com.Aditya.ZorvynAsssignment.Entity.UserStatus;
import com.Aditya.ZorvynAsssignment.Repository.RoleRepository;
import com.Aditya.ZorvynAsssignment.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(null, RoleName.VIEWER));
            roleRepository.save(new Role(null, RoleName.ANALYST));
            roleRepository.save(new Role(null, RoleName.ADMIN));
        }

        if (!userRepository.existsByUsername("admin")) {
            Role adminRole = roleRepository.findByName(RoleName.ADMIN).orElse(null);
            if (adminRole != null) {
                User admin = User.builder()
                        .username("admin")
                        .email("admin@example.com")
                        .password(passwordEncoder.encode("admin123"))
                        .role(adminRole)
                        .status(UserStatus.ACTIVE)
                        .build();
                userRepository.save(admin);
            }
        }

        if (!userRepository.existsByUsername("analyst")) {
            Role analystRole = roleRepository.findByName(RoleName.ANALYST).orElse(null);
            if (analystRole != null) {
                User analyst = User.builder()
                        .username("analyst")
                        .email("analyst@example.com")
                        .password(passwordEncoder.encode("analyst123"))
                        .role(analystRole)
                        .status(UserStatus.ACTIVE)
                        .build();
                userRepository.save(analyst);
            }
        }
    }
}