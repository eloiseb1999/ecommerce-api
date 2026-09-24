package com.portfolio.ecommerce.dto.request;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class UpdateCartItemRequest {

    /**
     * Nova quantidade do item. Se for 0, o item e removido do carrinho.
     */
    @PositiveOrZero(message = "A quantidade nao pode ser negativa")
    private int quantity;
}
