package com.portfolio.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {

    @NotBlank(message = "O nome da categoria e obrigatorio")
    private String name;

    private String description;
}
