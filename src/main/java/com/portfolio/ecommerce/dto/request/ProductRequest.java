package com.portfolio.ecommerce.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotBlank(message = "O nome do produto e obrigatorio")
    private String name;

    private String description;

    @NotNull(message = "O preco e obrigatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preco deve ser maior que zero")
    private BigDecimal price;

    @NotNull(message = "A quantidade em estoque e obrigatoria")
    @PositiveOrZero(message = "O estoque nao pode ser negativo")
    private Integer stockQuantity;

    @NotBlank(message = "A categoria e obrigatoria")
    private String categoryId;

    private String imageUrl;
}
