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

  private static final String ADMIN_ENDPOINT = "/api/admin/**";
  private static final String LOGIN_ENDPOINT = "/user/login";
  private static final String REG_ENDPOINT = "/user";

  private static final String[] PUBLIC_URLS = {
          "/v2/api-docs",
          "/swagger-ui/index.html",
          "/swagger-resources/**",
          "configuration/**",
          "webjars/**",
          "/*.html",
          "/**/*.html",
          "/**/*.css",
          "/**/*.js"
  };
  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(HttpMethod.POST,LOGIN_ENDPOINT).permitAll()
                    .requestMatchers(REG_ENDPOINT).permitAll()
                    .requestMatchers(ADMIN_ENDPOINT).permitAll()
                    .requestMatchers(HttpMethod.GET, PUBLIC_URLS).permitAll()
                    .requestMatchers("/swagger-ui/**",
                            "/swagger-resources/*",
                            "/v3/api-docs/**").permitAll()
            .and()
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class));
    return http.build();
  }
}
