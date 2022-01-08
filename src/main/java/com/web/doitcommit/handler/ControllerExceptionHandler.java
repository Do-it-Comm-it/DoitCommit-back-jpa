package com.web.doitcommit.handler;

import com.web.doitcommit.dto.CMRespDto;
import com.web.doitcommit.handler.exception.InvalidTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler  {

    //에러 처리
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<CMRespDto<?>> validationTokenException(InvalidTokenException e){

        return new ResponseEntity<>(new CMRespDto<>(-1,e.getMessage(),null), HttpStatus.UNAUTHORIZED);
    }
}
