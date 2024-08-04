package com.example.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ExceptionDto implements Serializable {
    private Integer status;
    private String message;
}
