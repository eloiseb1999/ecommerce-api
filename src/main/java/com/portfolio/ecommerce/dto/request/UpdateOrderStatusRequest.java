package com.portfolio.ecommerce.dto.request;

import com.portfolio.ecommerce.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {

    @NotNull(message = "O novo status e obrigatorio")
    private OrderStatus status;
}
