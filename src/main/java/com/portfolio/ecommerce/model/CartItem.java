package com.portfolio.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Item do carrinho. E armazenado embutido dentro do documento Cart,
 * guardando um "snapshot" do preco do produto no momento em que foi
 * adicionado, para nao ser afetado por futuras alteracoes de preco.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private String productId;
    private String productName;
    private BigDecimal unitPrice;
    private Integer quantity;

    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
