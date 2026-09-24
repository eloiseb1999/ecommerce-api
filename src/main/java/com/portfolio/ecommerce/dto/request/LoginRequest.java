package com.portfolio.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "O email e obrigatorio")
    private String email;

    @NotBlank(message = "A senha e obrigatoria")
    private String password;
}
