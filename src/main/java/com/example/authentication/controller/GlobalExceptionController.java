package com.example.authentication.controller;

import com.example.authentication.Exception.RequestException;
import com.example.authentication.dto.ExceptionDto;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionController{

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ExceptionDto> handleBadRequestException(final RequestException ex) {
        return new ResponseEntity<>(new ExceptionDto(HttpStatus.BAD_REQUEST.value(),ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}