package com.portfolio.ecommerce.controller;

import com.portfolio.ecommerce.dto.request.LoginRequest;
import com.portfolio.ecommerce.dto.request.RegisterRequest;
import com.portfolio.ecommerce.dto.response.AuthResponse;
import com.portfolio.ecommerce.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticacao", description = "Registro e login de usuarios")
@SecurityRequirements
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Cria uma nova conta de usuario (role padrao: USER)")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Autentica um usuario e retorna o token JWT")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
