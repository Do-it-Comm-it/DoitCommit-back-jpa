package com.web.doitcommit.config;

import com.web.doitcommit.config.oAuth.OAuth2DetailsService;
import com.web.doitcommit.config.oAuth.handler.OAuth2AuthenticationSuccessHandler;
import com.web.doitcommit.filter.JwtAuthenticationEntryPoint;
import com.web.doitcommit.filter.JwtAuthorizationFilter;
import com.web.doitcommit.jwt.JwtAccessDeniedHandler;
import com.web.doitcommit.jwt.JwtUtil;
import com.web.doitcommit.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OAuth2DetailsService oAuth2DetailsService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;



    @Autowired
    public SecurityConfig(OAuth2DetailsService oAuth2DetailsService, OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler, JwtAuthorizationFilter jwtAuthorizationFilter, JwtUtil jwtUtil, RedisService redisService) {
        this.oAuth2DetailsService = oAuth2DetailsService;
//        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
    }

    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler(){
        return new OAuth2AuthenticationSuccessHandler(jwtUtil, redisService);
    }

//    private final OAuth2DetailsService oAuth2DetailsService;
//    private final MemberRepository memberRepository;
//    private final RedisService redisService;
//    private final CookieUtil cookieUtil;
//    private final JwtUtil jwtUtil;
//
//    @Bean
//    public PasswordEncoder encode() {
//        return new BCryptPasswordEncoder();
//    }
//
////    @Bean
////    public JwtUtil jwtUtil(){
////        return new JwtUtil();
////    }
//
//
//    @Bean
//    public JwtAuthorizationFilter jwtAuthorizationFilter() {
//        return new JwtAuthorizationFilter("/auth/**/*", jwtUtil, memberRepository, cookieUtil);
//    }
//
//    @Bean
//    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint(){
//        return new JwtAuthenticationEntryPoint();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 사용하지 않겠다.
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable();

        http.oauth2Login()
                .userInfoEndpoint()
                .userService(oAuth2DetailsService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler())

                .and()
                .authorizeRequests()
                .antMatchers("/members/*").authenticated()
                .anyRequest().permitAll()

                .and()
//                .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler);
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
