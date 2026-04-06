package com.Aditya.ZorvynAsssignment.Config;

import com.Aditya.ZorvynAsssignment.Entity.Role;
import com.Aditya.ZorvynAsssignment.Entity.RoleName;
import com.Aditya.ZorvynAsssignment.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(null, RoleName.VIEWER));
            roleRepository.save(new Role(null, RoleName.ANALYST));
            roleRepository.save(new Role(null, RoleName.ADMIN));
        }
    }
}
