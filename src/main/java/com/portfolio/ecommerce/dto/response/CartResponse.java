package com.portfolio.ecommerce.dto.response;

import com.portfolio.ecommerce.model.Cart;
import com.portfolio.ecommerce.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private String id;
    private String userId;
    private List<CartItem> items;
    private BigDecimal totalAmount;

    public static CartResponse fromEntity(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .items(cart.getItems())
                .totalAmount(total)
                .build();
    }
}
