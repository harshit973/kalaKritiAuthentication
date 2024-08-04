package com.example.authentication.repository;

import com.example.authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long> {
    public static final String PROPERTY_NAME="{property}";
    public static final String FIND_USER="select * from public_user where "+PROPERTY_NAME+"=:value";
}
