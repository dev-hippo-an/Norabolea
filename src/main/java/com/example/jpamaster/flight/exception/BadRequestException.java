package com.example.jpamaster.flight.exception;


import com.example.jpamaster.common.enums.HttpStatusCode;
import com.example.jpamaster.common.exception.CommonException;
import lombok.Getter;

@Getter
public class BadRequestException extends CommonException {


    public BadRequestException(String message) {
        super(HttpStatusCode.BAD_REQUEST, message);
    }
}
