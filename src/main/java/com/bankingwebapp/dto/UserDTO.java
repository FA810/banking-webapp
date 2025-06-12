package com.bankingwebapp.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String role;
    private String email;
}
