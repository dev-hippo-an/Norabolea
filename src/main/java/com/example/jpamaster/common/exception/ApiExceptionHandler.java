package com.example.jpamaster.common.exception;

import com.example.jpamaster.common.ApiResponse;
import com.example.jpamaster.common.enums.HttpStatusCode;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
@RequiredArgsConstructor
@ApiOperation(value = "예외 처리", hidden = true)
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = JpaMasterBadRequest.class)
    public ApiResponse<Void> handler(JpaMasterBadRequest e) {
        log.error("Bad Request: {}", e.getMessage(), e);
        return ApiResponse.createError(e.getHttpStatusCode(), e.getMessage());
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = JpaMasterNotFoundException.class)
    public ApiResponse<Void> handler(JpaMasterNotFoundException e) {
        log.error("Not Found: {}", e.getMessage(), e);
        return ApiResponse.createError(e.getHttpStatusCode(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = KakaoResException.class)
    public ApiResponse<Void> kakaoErrorHandler(KakaoResException e)
    {
        log.error("Kakao Client Error : {}", e.getMessage(), e);
        return ApiResponse.createError(e.getHttpStatusCode(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ApiResponse<Void> handler(Exception e) {
        log.error("Internal Server error: {}", e.getMessage(), e);
        return ApiResponse.createError(
                HttpStatusCode.INTERNAL_SERVER_ERROR, e.getMessage()
        );
    }
}
