package com.portfolio.ecommerce.controller;

import com.portfolio.ecommerce.dto.request.CheckoutRequest;
import com.portfolio.ecommerce.dto.response.OrderResponse;
import com.portfolio.ecommerce.security.CustomUserDetails;
import com.portfolio.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Checkout e historico de pedidos do usuario autenticado")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    @Operation(summary = "Fecha o pedido a partir do carrinho atual do usuario")
    public ResponseEntity<OrderResponse> checkout(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                   @Valid @RequestBody CheckoutRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.checkout(userDetails.getId(), request));
    }

    @GetMapping
    @Operation(summary = "Lista os pedidos do usuario autenticado")
    public ResponseEntity<Page<OrderResponse>> findMyOrders(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                             Pageable pageable) {
        return ResponseEntity.ok(orderService.findByUser(userDetails.getId(), pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um pedido pelo id (somente o dono do pedido pode ver)")
    public ResponseEntity<OrderResponse> findById(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                   @PathVariable String id) {
        return ResponseEntity.ok(orderService.findByIdForUser(id, userDetails.getId()));
    }
}
