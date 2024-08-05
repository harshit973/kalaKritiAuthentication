package com.example.authentication.service;

import com.example.authentication.dto.UserDto;
import com.example.authentication.entity.User;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface UserService {
    List<User> getUsersByPropertyName(final String property, final String value) throws Exception;
    User createUser(final UserDto userDto);
    User updateUser(final Long id,final UserDto userDto);
    Boolean deleteUser(final Long id);
    String login(String email, String password, HttpServletResponse response) throws Exception;

}
