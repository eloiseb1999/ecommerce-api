package com.portfolio.ecommerce.controller;

import com.portfolio.ecommerce.dto.request.ProductRequest;
import com.portfolio.ecommerce.dto.response.ProductResponse;
import com.portfolio.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "Catalogo de produtos: consulta publica e gerenciamento por ADMIN")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Lista produtos ativos, com paginacao e filtros opcionais (acesso publico)")
    public ResponseEntity<Page<ProductResponse>> findAll(
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Pageable pageable) {
        return ResponseEntity.ok(productService.findAll(categoryId, name, minPrice, maxPrice, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um produto pelo id (acesso publico)")
    public ResponseEntity<ProductResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo produto (somente ADMIN)")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um produto existente (somente ADMIN)")
    public ResponseEntity<ProductResponse> update(@PathVariable String id, @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove (desativa) um produto do catalogo (somente ADMIN)")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
