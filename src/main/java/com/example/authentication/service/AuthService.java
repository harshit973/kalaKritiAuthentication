package com.example.authentication.service;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    String hashPassword(String plainPassword);
    Boolean checkPassword(String plainPassword, String hashedPassword);
}
