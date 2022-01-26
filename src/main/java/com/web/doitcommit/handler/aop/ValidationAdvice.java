package com.web.doitcommit.handler.aop;

import com.web.doitcommit.handler.exception.CustomValidationException;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
@Aspect
public class ValidationAdvice {

    @Around("execution(* com.web.doitcommit.controller.*Controller.*(..))")
    public Object advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Object[] args = proceedingJoinPoint.getArgs();

        for (Object arg : args){
            if(arg instanceof BindingResult){
                BindingResult bindingResult = (BindingResult) arg;

                if(bindingResult.hasErrors()){
                    log.info("유효성 검사에 실패하였습니다.");
                    Map<String,String> errorMap = new HashMap<>();

                    for(FieldError error: bindingResult.getFieldErrors()){
                        errorMap.put(error.getField(),error.getDefaultMessage());
                        log.info(error.getDefaultMessage());
                    }
                    throw new CustomValidationException("유효성 검사 실패", errorMap);
                }
            }
        }
        return proceedingJoinPoint.proceed();
    }

}
