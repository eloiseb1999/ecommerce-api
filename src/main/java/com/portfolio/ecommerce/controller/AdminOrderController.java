package com.portfolio.ecommerce.controller;

import com.portfolio.ecommerce.dto.request.UpdateOrderStatusRequest;
import com.portfolio.ecommerce.dto.response.OrderResponse;
import com.portfolio.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@Tag(name = "Admin - Pedidos", description = "Gerenciamento de pedidos de todos os usuarios (somente ADMIN)")
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Lista todos os pedidos da loja, de todos os usuarios")
    public ResponseEntity<Page<OrderResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(orderService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca qualquer pedido pelo id")
    public ResponseEntity<OrderResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualiza o status de um pedido (ex: PAID, SHIPPED, DELIVERED, CANCELED)")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable String id,
                                                       @Valid @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(orderService.updateStatus(id, request.getStatus()));
    }
}
