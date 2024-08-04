package com.example.authentication.controller;

import com.example.authentication.dto.UserDto;
import com.example.authentication.entity.User;
import com.example.authentication.repository.UserRepository;
import com.example.authentication.service.AuthService;
import com.example.authentication.service.UserService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/auth")
public class UserController {

    @Autowired
    UserService userService;

    @QueryMapping
    public List<User> getUserByPropertyName(@Argument String propertyName, @Argument String value) {
        try{
            return this.userService.getUsersByPropertyName(propertyName,value);
        }catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody UserDto userDto, HttpServletResponse response) throws Exception {
        this.userService.login(userDto.getEmail(), userDto.getPassword(),response);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


    @MutationMapping
    public User createUser(@Argument UserDto userDto) {
        return this.userService.createUser(userDto);
    }
}
