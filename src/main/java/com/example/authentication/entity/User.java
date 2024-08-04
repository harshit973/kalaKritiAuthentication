package com.example.authentication.entity;

import com.example.authentication.entity.util.EntityConstants;
import jakarta.persistence.*;
import lombok.Data;

@Entity(name= EntityConstants.PUBLIC_USER)
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false)
    private String contact;
    private Integer age;
    private Boolean gender;
    @Column(nullable = false)
    private String password;

}
