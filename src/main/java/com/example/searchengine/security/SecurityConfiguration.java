package com.example.searchengine.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration{

  private final JWTTokenFilter jwtTokenFilter;

  private static final String LOGIN_ENDPOINT = "/user/login";
  private static final String REG_ENDPOINT = "/user";
 // private static final String TEST_ENDPOINT = "/user";
  private static final String[] SITE_ENDPOINTS = {"/site","/site/**"};

  private static final String SEARCH_ENDPOINT = "/search";
  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
            .authorizeHttpRequests(authorize -> authorize
                    //.requestMatchers(TEST_ENDPOINT).permitAll()
                    .requestMatchers(HttpMethod.GET,SEARCH_ENDPOINT).permitAll()
                    .requestMatchers(HttpMethod.POST,LOGIN_ENDPOINT).permitAll()
                    .requestMatchers(HttpMethod.POST,REG_ENDPOINT).permitAll()
                    .requestMatchers(SITE_ENDPOINTS).hasAnyAuthority("user")
                    .requestMatchers("/swagger-ui/**",
                            "/swagger-resources/*",
                            "/v3/api-docs/**").permitAll()
            .and()
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class));
    return http.build();
  }
}
