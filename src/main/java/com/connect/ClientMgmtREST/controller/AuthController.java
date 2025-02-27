package com.connect.ClientMgmtREST.controller;


import com.connect.ClientMgmtREST.dto.request.AuthenticationRequest;
import com.connect.ClientMgmtREST.dto.response.AuthenticationResponse;
import com.connect.ClientMgmtREST.dto.request.RegisterRequest;

import com.connect.ClientMgmtREST.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/demo")
    public ResponseEntity<String> demo() {
        return ResponseEntity.ok("Hello from the top of the World");
    }
}




