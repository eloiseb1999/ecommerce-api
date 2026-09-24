package com.portfolio.ecommerce.controller;

import com.portfolio.ecommerce.dto.request.CategoryRequest;
import com.portfolio.ecommerce.dto.response.CategoryResponse;
import com.portfolio.ecommerce.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Consulta e gerenciamento de categorias de produtos")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Lista todas as categorias (acesso publico)")
    public ResponseEntity<List<CategoryResponse>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma categoria pelo id (acesso publico)")
    public ResponseEntity<CategoryResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Cria uma nova categoria (somente ADMIN)")
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma categoria existente (somente ADMIN)")
    public ResponseEntity<CategoryResponse> update(@PathVariable String id, @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove uma categoria (somente ADMIN)")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
