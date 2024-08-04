package com.example.authentication.service;

import com.example.authentication.dto.UserDto;
import com.example.authentication.entity.User;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface UserService {
    List<User> getUsersByPropertyName(final String property, final String value) throws Exception;
    User createUser(final UserDto userDto);
    String login(String email, String password, HttpServletResponse response) throws Exception;

}
