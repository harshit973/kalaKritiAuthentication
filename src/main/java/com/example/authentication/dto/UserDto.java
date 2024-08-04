package com.example.authentication.dto;

import lombok.Data;

@Data
public class UserDto {
    private String name;
    private String email;
    private String contact;
    private Integer age;
    private Boolean gender;
    private String password;
}
