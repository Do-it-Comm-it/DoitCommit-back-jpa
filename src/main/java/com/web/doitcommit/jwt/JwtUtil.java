package com.web.doitcommit.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JwtUtil implements InitializingBean {

    @Value("${app.token.secretKey}")
    private String secretKey;

    @Value("${app.token.accessTokenExpire}")
    private long accessTokenExpire;

    @Value("${app.token.refreshTokenExpire}")
    private long refreshTokenExpire;

    private Key key;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    //accessToken 생성
    public String generateAccessToken(Long memberId) {

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(accessTokenExpire).toInstant()))
                .claim("memberId", memberId)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //refreshToken 생성
    public String generateRefreshToken(Long memberId) {

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(refreshTokenExpire).toInstant()))
                .claim("memberId", memberId)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //토큰 검증
    public Long validateAndExtract(String tokenStr){

            try{
                Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(tokenStr).getBody();
                Long userId = claims.get("memberId", Long.class);
                return userId;

            }catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
                log.info("잘못된 JWT 서명입니다.");
            } catch (ExpiredJwtException e) {
                log.info("만료된 JWT 토큰입니다.");
            } catch (UnsupportedJwtException e) {
                log.info("지원되지 않는 JWT 토큰입니다.");
            } catch (IllegalArgumentException e) {
                log.info("JWT 토큰이 잘못되었습니다.");
            }
            return null;
    }
}
