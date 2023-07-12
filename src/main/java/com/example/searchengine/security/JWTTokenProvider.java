package com.example.searchengine.security;

import com.example.searchengine.entity.Role;
import io.jsonwebtoken.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class JWTTokenProvider {

  @Value("${jwt.token.secret}")
  private String jwtSecret;

  @Value("${jwt.token.expired}")
  private long jwtExpirationInMs;

  @Autowired
  private UserDetailsService userDetailsService;

  @PostConstruct
  protected void init() {
    jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
  }

  public String generateToken(String username, Set<Role> roles){
    Claims claims = Jwts.claims().setSubject(username);
    claims.put("roles", getUserRoleNamesFromJWT(roles));

    Date now = new Date();
    Date validity = new Date(now.getTime() + jwtExpirationInMs);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(SignatureAlgorithm.HS256, jwtSecret)
        .compact();
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(getUserUsernameFromJWT(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUserUsernameFromJWT(String token){
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  public String resolveToken(HttpServletRequest req) {
    String bearerToken = req.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer_")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  @SneakyThrows
  public  boolean validateToken(String token) {
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);

      return !claims.getBody().getExpiration().before(new Date());
    } catch (JwtException | IllegalArgumentException e) {
      throw new JWTAuthenticationException("JWT token is expired or invalid");
    }
  }

  private List<String> getUserRoleNamesFromJWT(Set<Role> roles){
    List<String> result = new ArrayList<>();
    roles.forEach(role -> result.add(role.name()));
    return result;
  }
}
