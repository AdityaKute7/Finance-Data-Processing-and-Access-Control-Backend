package com.Aditya.ZorvynAsssignment.Services;

import com.Aditya.ZorvynAsssignment.DTOs.AuthResponse;
import com.Aditya.ZorvynAsssignment.DTOs.LoginRequest;
import com.Aditya.ZorvynAsssignment.DTOs.RegisterRequest;
import com.Aditya.ZorvynAsssignment.Entity.Role;
import com.Aditya.ZorvynAsssignment.Entity.RoleName;
import com.Aditya.ZorvynAsssignment.Entity.User;
import com.Aditya.ZorvynAsssignment.Entity.UserStatus;
import com.Aditya.ZorvynAsssignment.Exception.BadRequestException;
import com.Aditya.ZorvynAsssignment.Repository.RoleRepository;
import com.Aditya.ZorvynAsssignment.Repository.UserRepository;
import com.Aditya.ZorvynAsssignment.security.JwtTokenProvider;
//import com.Aditya.dto.AuthResponse;
//import com.Aditya.dto.LoginRequest;
//import com.Aditya.dto.RegisterRequest;
//import com.Aditya.entity.Role;
//import com.Aditya.entity.RoleName;
//import com.Aditya.entity.User;
//import com.Aditya.entity.UserStatus;
//import com.Aditya.exception.BadRequestException;
//import com.Aditya.repository.RoleRepository;
//import com.Aditya.repository.UserRepository;
//import com.Aditya.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already in use");
        }


        RoleName roleName = request.getRole() != null             // use role from request,defualt VIEWER
                ? request.getRole()
                : RoleName.VIEWER;

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new BadRequestException("Role not found"));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        String token = jwtTokenProvider.generateToken(authentication);

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().getName().name())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = jwtTokenProvider.generateToken(authentication);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("User not found"));

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().getName().name())
                .build();
    }
}
