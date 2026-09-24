package com.portfolio.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddCartItemRequest {

    @NotBlank(message = "O productId e obrigatorio")
    private String productId;

    @Positive(message = "A quantidade deve ser maior que zero")
    private int quantity;
}
