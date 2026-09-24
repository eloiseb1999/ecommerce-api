package com.portfolio.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckoutRequest {

    @NotBlank(message = "O endereco de entrega e obrigatorio")
    private String shippingAddress;
}
