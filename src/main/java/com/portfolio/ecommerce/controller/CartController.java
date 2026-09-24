package com.portfolio.ecommerce.controller;

import com.portfolio.ecommerce.dto.request.AddCartItemRequest;
import com.portfolio.ecommerce.dto.request.UpdateCartItemRequest;
import com.portfolio.ecommerce.dto.response.CartResponse;
import com.portfolio.ecommerce.security.CustomUserDetails;
import com.portfolio.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Carrinho", description = "Carrinho de compras do usuario autenticado")
public class CartController {

    private final CartService cartService;

    @GetMapping
    @Operation(summary = "Retorna o carrinho do usuario autenticado")
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(cartService.getCart(userDetails.getId()));
    }

    @PostMapping("/items")
    @Operation(summary = "Adiciona um produto ao carrinho (soma a quantidade se ja existir)")
    public ResponseEntity<CartResponse> addItem(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @Valid @RequestBody AddCartItemRequest request) {
        return ResponseEntity.ok(cartService.addItem(userDetails.getId(), request));
    }

    @PutMapping("/items/{productId}")
    @Operation(summary = "Atualiza a quantidade de um item (quantidade 0 remove o item)")
    public ResponseEntity<CartResponse> updateItem(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                    @PathVariable String productId,
                                                    @Valid @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(cartService.updateItemQuantity(userDetails.getId(), productId, request.getQuantity()));
    }

    @DeleteMapping("/items/{productId}")
    @Operation(summary = "Remove um item do carrinho")
    public ResponseEntity<CartResponse> removeItem(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                    @PathVariable String productId) {
        return ResponseEntity.ok(cartService.removeItem(userDetails.getId(), productId));
    }

    @DeleteMapping
    @Operation(summary = "Esvazia o carrinho")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        cartService.clearCart(userDetails.getId());
        return ResponseEntity.noContent().build();
    }
}
