package com.web.doitcommit.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @className : JwtAccessDeniedHandler
 * @description : 토큰 인증 403 에러 클래스
 * @date : 2021-04-05
 * @author : parksujin
 * @version : 1.0.0
 * @see
 * @history :
 **/
@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

   /**
     * @name handle
     * @description 토큰 인증 에러시 에러정보를 보여준다
     * - 파라미터 정보
     *  1. request : HttpServletRequest 인터페이스 객체
     *  2. response : HttpServletResponse 인터페이스 객체
     *  3. accessDeniedException : AccessDeniedException 객체
     * @throws IOException : 예외
     * @author : parksujin
     */
   @Override
   public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
      Map<String, Object> resultMap = new HashMap<>();
      resultMap.put("code", HttpServletResponse.SC_FORBIDDEN);
      resultMap.put("msg", accessDeniedException.getMessage());
      resultMap.put("serverTime", System.currentTimeMillis());
      try {
         ObjectMapper mapper = new ObjectMapper();
         response.getWriter().write(mapper.writeValueAsString(resultMap));
         response.getWriter().flush();
      }catch (JsonProcessingException e) {
         log.error(e.getMessage());
      }finally {
         response.getWriter().close();
      }
   }
}