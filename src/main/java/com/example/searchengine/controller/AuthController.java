package com.example.searchengine.controller;

import com.example.searchengine.entity.User;
import com.example.searchengine.exception.RoleNotFoundException;
import com.example.searchengine.response.RegistrationResponse;
import com.example.searchengine.response.Response;
import com.example.searchengine.response.ResponseStatus;
import com.example.searchengine.security.JWTTokenProvider;
import com.example.searchengine.service.UserService;
import com.example.searchengine.task.SiteCrawlerTask;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/user")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Autowired SiteCrawlerTask siteCrawlerTask;
    private static final String SAME_NAME_ERROR_MESSAGE = "A user with the same name already exists";
    private static final String USER_CREATED_SUCCESS_MESSAGE = "User created";
    private static final String UNEXPECTED_REGISTER_ERROR_MESSAGE = "An error occurred while registering the user";


    private BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostMapping
    public ResponseEntity<Response> registration(@Valid @RequestBody User user, BindingResult bindingResult) throws RoleNotFoundException {
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            String errorMessage = errors.stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new Response(ResponseStatus.ERROR, errorMessage));
        }
        try {
            if (userService.existsByUsername(user.getUsername())) {
                return ResponseEntity.badRequest().body(new Response(ResponseStatus.ERROR,SAME_NAME_ERROR_MESSAGE));
            }
            userService.save(user);
            return ResponseEntity.ok(new RegistrationResponse(ResponseStatus.SUCCESS, USER_CREATED_SUCCESS_MESSAGE, user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(ResponseStatus.ERROR, UNEXPECTED_REGISTER_ERROR_MESSAGE));
        }
    }

    @GetMapping
    public ResponseEntity<String> test(){
        siteCrawlerTask.execute();
        return ResponseEntity.ok().build();
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

