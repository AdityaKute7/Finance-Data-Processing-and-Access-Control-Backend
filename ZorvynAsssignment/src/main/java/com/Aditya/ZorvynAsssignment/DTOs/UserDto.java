package com.Aditya.ZorvynAsssignment.DTOs;

import com.Aditya.ZorvynAsssignment.Entity.UserStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private String role;
    private UserStatus status;
}
