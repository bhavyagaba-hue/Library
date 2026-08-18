package com.Project.Library.dto;

import com.Project.Library.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserResponse {
    private Integer userId;
    private String name;
    private String email;
    private Role role;
    private LocalDate joinedOn;
}