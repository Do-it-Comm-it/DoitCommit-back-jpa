package com.web.doitcommit.controller;

import com.web.doitcommit.config.auth.PrincipalDetails;
import com.web.doitcommit.dto.CMRespDto;
import com.web.doitcommit.jwt.CookieUtil;
import com.web.doitcommit.jwt.JwtUtil;
import com.web.doitcommit.redis.RedisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RedisService redisService;

    @Operation(summary = "토큰 재발급 API", description = "refresh token으로 access token과 refresh token을 재발급한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(example = "{\n" +
                    "  \"message\": \"토큰 재발급 완료\",\n" +
                    "  \"data\": null,\n" +
                    "  \"code\": 1\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @GetMapping("/auth/refreshToken")
    public ResponseEntity<?> verifyRefreshToken(@Parameter(name = "refreshToken", hidden = true) @CookieValue("refreshToken") String refreshToken,
                                                        HttpServletResponse response){

        //refreshToken 검증
        Long memberId = jwtUtil.validateAndExtract(refreshToken);

        //잘못된 refreshToken 일 경우
        if (memberId == null){
            return new ResponseEntity<>(new CMRespDto<>(1,"UnAuthorized", null),HttpStatus.UNAUTHORIZED);
        }

        //redis 에 등록된 refreshToken 가져오기
        String findRefreshToken = redisService.getValues(memberId);

        //잘못된 refreshToken 일 경우
        if(findRefreshToken == null || !findRefreshToken.equals(refreshToken) || refreshToken == null){
            return new ResponseEntity<>(new CMRespDto<>(1,"UnAuthorized", null),HttpStatus.UNAUTHORIZED);
        }

        //새로운 accessToken, refreshToken 생성
        String newAccessToken = jwtUtil.generateAccessToken(memberId);
        String newRefreshToken = jwtUtil.generateRefreshToken(memberId);

        redisService.setValues(memberId, newRefreshToken);

        //쿠키에 accessToken, refreshToken 저장
        cookieUtil.createCookie(response, jwtUtil.accessTokenName, newAccessToken, jwtUtil.accessTokenExpire);
        cookieUtil.createCookie(response, jwtUtil.refreshTokenName, newRefreshToken,jwtUtil.refreshTokenExpire);

        return new ResponseEntity<>(new CMRespDto<>(1,"토큰 재발급 완료", null), HttpStatus.OK);
    }

    @Operation(summary = "로그아웃 API", description = "로그아웃을 한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(example = "{\n" +
                    "  \"message\": \"로그아웃 성공\",\n" +
                    "  \"data\": null,\n" +
                    "  \"code\": 1\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(example = "{\"error\": \"Bad Request\"}"))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(example = "{\"error\": \"Unauthorized\"}"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(example = "{\"error\": \"Not Found\"}"))),
            @ApiResponse(responseCode = "500",  content = @Content(schema = @Schema(example = "{\"error\": \"Internal Server Error\"}")))
    })
    @PostMapping("/auth/logout")
    public ResponseEntity<?> doLogout(HttpServletRequest request, HttpServletResponse response, @CookieValue("refreshToken") String refreshToken) {
        Cookie accessTokenCookie = cookieUtil.getCookie(request, jwtUtil.accessTokenName);
        Cookie refreshTokenCookie = cookieUtil.getCookie(request, jwtUtil.refreshTokenName);
        Long memberId = jwtUtil.validateAndExtract(refreshToken);
        String refreshTokenRedis = redisService.getValues(memberId);

        if(accessTokenCookie != null) {
            cookieUtil.createCookie(response, jwtUtil.accessTokenName,accessTokenCookie.getValue(), 0);
        }
        if(refreshTokenCookie != null) {
            cookieUtil.createCookie(response, jwtUtil.refreshTokenName, refreshTokenCookie.getValue(), 0);
        }
        if(refreshTokenRedis != null) {
            redisService.delValues(memberId);
        }
        return new ResponseEntity<>(new CMRespDto<>(1,"로그아웃 성공", null),HttpStatus.OK);
    }


}
