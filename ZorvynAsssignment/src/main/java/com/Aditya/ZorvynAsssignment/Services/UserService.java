package com.Aditya.ZorvynAsssignment.Services;

import com.Aditya.ZorvynAsssignment.DTOs.RegisterRequest;
import com.Aditya.ZorvynAsssignment.DTOs.UserDto;
import com.Aditya.ZorvynAsssignment.Entity.Role;
import com.Aditya.ZorvynAsssignment.Entity.RoleName;
import com.Aditya.ZorvynAsssignment.Entity.User;
import com.Aditya.ZorvynAsssignment.Entity.UserStatus;
import com.Aditya.ZorvynAsssignment.Exception.BadRequestException;
import com.Aditya.ZorvynAsssignment.Exception.ResourceNotFoundException;
import com.Aditya.ZorvynAsssignment.Repository.RoleRepository;
import com.Aditya.ZorvynAsssignment.Repository.UserRepository;
//import com.Aditya.dto.RegisterRequest;
//import com.Aditya.dto.UserDto;
//import com.Aditya.entity.Role;
//import com.Aditya.entity.RoleName;
//import com.Aditya.entity.User;
//import com.Aditya.entity.UserStatus;
//import com.Aditya.exception.BadRequestException;
//import com.Aditya.exception.ResourceNotFoundException;
//import com.Aditya.repository.RoleRepository;
//import com.Aditya.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public UserDto createUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already in use");
        }

        RoleName roleName = request.getRole() != null ? request.getRole() : RoleName.VIEWER;
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .status(UserStatus.ACTIVE)
                .build();

        user = userRepository.save(user);
        return toDto(user);
    }

    private UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().getName().name())
                .status(user.getStatus())
                .build();
    }
}
