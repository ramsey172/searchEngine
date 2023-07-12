package com.example.searchengine.controller;

import com.example.searchengine.entity.User;
import com.example.searchengine.security.JWTTokenProvider;
import com.example.searchengine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    private BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostMapping
    public ResponseEntity<String> registration(@RequestBody User user) {
        try {
            if (userService.existsByUsername(user.getUsername())) {
                return ResponseEntity.badRequest().body("A user with the same name already exists");
            }
            userService.save(user);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while registering the user");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequestDTO dto) {
        User byUsername = userService.findByUsername(dto.username());
        if(passwordEncoder().matches(dto.password(), byUsername.getPassword())) {
            String token = jwtTokenProvider.generateToken(dto.username(), byUsername.getRoles());
            return ResponseEntity.ok(token);
        }else{
            return ResponseEntity.badRequest().body("Invalid username or password");
        }


    }
}

record AuthRequestDTO(String username, String password) {
}
