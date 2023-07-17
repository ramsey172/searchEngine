package com.example.searchengine.controller;

import com.example.searchengine.entity.User;
import com.example.searchengine.exception.RoleNotFoundException;
import com.example.searchengine.response.RegistrationResponse;
import com.example.searchengine.response.Response;
import com.example.searchengine.response.ResponseStatus;
import com.example.searchengine.security.JWTTokenProvider;
import com.example.searchengine.service.UserService;
import com.example.searchengine.task.SiteCrawlerTask;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JWTTokenProvider jwtTokenProvider;

    @Mock
    private SiteCrawlerTask siteCrawlerTask;

    private AuthController authController;

    private Validator validator;

    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController();
        authController.userService = userService;
        authController.jwtTokenProvider = jwtTokenProvider;
        authController.siteCrawlerTask = siteCrawlerTask;

        validator = Validation.buildDefaultValidatorFactory().getValidator();
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void registration_WithValidUser_ReturnsSuccessResponse() throws RoleNotFoundException {
        User user = new User();
        user.setName("name");
        user.setPassword("12345678");
        user.setUsername("username");
        when(userService.existsByUsername(user.getUsername())).thenReturn(false);

        ResponseEntity<Response> responseEntity = authController.registration(user, mock(BindingResult.class));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(ResponseStatus.SUCCESS, responseEntity.getBody().getStatus());
        assertEquals("User created", ((RegistrationResponse) responseEntity.getBody()).getMessage());
        assertEquals(user, ((RegistrationResponse) responseEntity.getBody()).getUser());

        verify(userService, times(1)).existsByUsername(user.getUsername());
        verify(userService, times(1)).save(user);
    }

    @Test
    void registration_WithExistingUsername_ReturnsErrorResponse() throws RoleNotFoundException {
        User user = new User();
        user.setName("name");
        user.setPassword("12345678");
        user.setUsername("username");
        when(userService.existsByUsername(user.getUsername())).thenReturn(true);

        ResponseEntity<Response> responseEntity = authController.registration(user, mock(BindingResult.class));

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(ResponseStatus.ERROR, responseEntity.getBody().getStatus());
        assertEquals("A user with the same name already exists", responseEntity.getBody().getMessage());

        verify(userService, times(1)).existsByUsername(user.getUsername());
        verify(userService, never()).save(user);
    }

    @Test
    void registration_WithInvalidUser_ReturnsErrorResponse() throws RoleNotFoundException {
        User user = new User();
        user.setName("");
        user.setPassword("");
        user.setUsername("");
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        List<ObjectError> errors = new ArrayList<>();
        errors.add(new ObjectError("user", "Invalid username"));
        when(bindingResult.getAllErrors()).thenReturn(errors);

        ResponseEntity<Response> responseEntity = authController.registration(user, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(ResponseStatus.ERROR, responseEntity.getBody().getStatus());
        assertEquals("Invalid username", responseEntity.getBody().getMessage());

        verify(bindingResult, times(1)).hasErrors();
        verify(bindingResult, times(1)).getAllErrors();
        verify(userService, never()).existsByUsername(user.getUsername());
        verify(userService, never()).save(user);
    }
}
