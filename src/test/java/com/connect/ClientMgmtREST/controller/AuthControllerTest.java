package com.connect.ClientMgmtREST.controller;

import com.connect.ClientMgmtREST.dto.request.AuthenticationRequest;
import com.connect.ClientMgmtREST.dto.response.AuthenticationResponse;
import com.connect.ClientMgmtREST.dto.request.RegisterRequest;
import com.connect.ClientMgmtREST.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegister() {
        RegisterRequest registerRequest = new RegisterRequest();
        AuthenticationResponse authResponse = new AuthenticationResponse("token");

        Mockito.when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        ResponseEntity<AuthenticationResponse> response = authController.register(registerRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token", response.getBody().getJwt());
    }

    @Test
    public void testAuthenticate() {
        AuthenticationRequest authRequest = new AuthenticationRequest();
        AuthenticationResponse authResponse = new AuthenticationResponse("token");

        Mockito.when(authService.authenticate(any(AuthenticationRequest.class))).thenReturn(authResponse);

        ResponseEntity<AuthenticationResponse> response = authController.authenticate(authRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token", response.getBody().getJwt());
    }

    @Test
    public void testDemo() {
        ResponseEntity<String> response = authController.demo();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hello from the top of the World", response.getBody());
    }
}
