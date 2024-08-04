package com.example.authentication.Exception;

import org.hibernate.service.spi.ServiceException;

public class BadRequestException extends ServiceException implements RequestException {

    Integer statusCode;
    String message;
    public BadRequestException(final String message){
        super(message);
        this.statusCode=400;
        this.message=message;
    }
    @Override
    public Integer getStatusCode() {
        return this.statusCode;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
