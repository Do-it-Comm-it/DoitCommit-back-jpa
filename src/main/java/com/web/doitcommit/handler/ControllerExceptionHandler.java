package com.web.doitcommit.handler;

import com.web.doitcommit.dto.CMRespDto;
import com.web.doitcommit.handler.exception.CustomException;
import com.web.doitcommit.handler.exception.CustomValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler {

    //유효성 검사
    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<CMRespDto<?>> validationApiException(CustomValidationException e){

        return new ResponseEntity<>(new CMRespDto<>(-1,e.getMessage(),e.errorMap), HttpStatus.BAD_REQUEST);
    }

    //에러 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CMRespDto<?>> apiException(CustomException e){

        return new ResponseEntity<>(new CMRespDto<>(-1,e.getMessage(),null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<CMRespDto<?>> apiSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e){
        return new ResponseEntity<>(new CMRespDto<>(-1,e.getMessage(),null), HttpStatus.BAD_REQUEST);
    }

}
