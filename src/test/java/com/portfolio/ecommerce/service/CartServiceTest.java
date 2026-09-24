package com.portfolio.ecommerce.service;

import com.portfolio.ecommerce.dto.request.AddCartItemRequest;
import com.portfolio.ecommerce.dto.response.CartResponse;
import com.portfolio.ecommerce.exception.BadRequestException;
import com.portfolio.ecommerce.model.Cart;
import com.portfolio.ecommerce.model.Product;
import com.portfolio.ecommerce.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartService cartService;

    @Test
    void deveAdicionarItemAoCarrinhoQuandoHaEstoque() {
        Product product = Product.builder()
                .id("prod-1").name("Fone de ouvido")
                .price(new BigDecimal("150.00")).stockQuantity(5).build();

        when(productService.getProductOrThrow("prod-1")).thenReturn(product);
        when(cartRepository.findByUserId("user-1")).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AddCartItemRequest request = new AddCartItemRequest();
        request.setProductId("prod-1");
        request.setQuantity(2);

        CartResponse response = cartService.addItem("user-1", request);

        assertThat(response.getItems()).hasSize(1);
        assertThat(response.getItems().get(0).getQuantity()).isEqualTo(2);
        assertThat(response.getTotalAmount()).isEqualByComparingTo("300.00");
    }

    @Test
    void naoDeveAdicionarItemQuandoEstoqueInsuficiente() {
        Product product = Product.builder()
                .id("prod-1").name("Monitor").price(new BigDecimal("900.00")).stockQuantity(1).build();

        when(productService.getProductOrThrow("prod-1")).thenReturn(product);

        AddCartItemRequest request = new AddCartItemRequest();
        request.setProductId("prod-1");
        request.setQuantity(5);

        assertThatThrownBy(() -> cartService.addItem("user-1", request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Estoque insuficiente");
    }
}
