package com.portfolio.ecommerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "O nome e obrigatorio")
    private String name;

    @NotBlank(message = "O email e obrigatorio")
    @Email(message = "Email invalido")
    private String email;

    @NotBlank(message = "A senha e obrigatoria")
    @Size(min = 6, message = "A senha deve ter no minimo 6 caracteres")
    private String password;
}
