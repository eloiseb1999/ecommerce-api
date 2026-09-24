package com.portfolio.ecommerce.service;

import com.portfolio.ecommerce.dto.request.ProductRequest;
import com.portfolio.ecommerce.dto.response.ProductResponse;
import com.portfolio.ecommerce.exception.ResourceNotFoundException;
import com.portfolio.ecommerce.model.Category;
import com.portfolio.ecommerce.model.Product;
import com.portfolio.ecommerce.repository.CategoryRepository;
import com.portfolio.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Lista produtos ativos, com filtros opcionais por categoria, nome (busca parcial)
     * ou faixa de preco. Apenas um filtro e aplicado por vez, na ordem: categoria, nome, preco.
     */
    public Page<ProductResponse> findAll(String categoryId, String name, BigDecimal minPrice,
                                          BigDecimal maxPrice, Pageable pageable) {
        Page<Product> page;

        if (categoryId != null && !categoryId.isBlank()) {
            page = productRepository.findByActiveTrueAndCategoryId(categoryId, pageable);
        } else if (name != null && !name.isBlank()) {
            page = productRepository.findByActiveTrueAndNameContainingIgnoreCase(name, pageable);
        } else if (minPrice != null && maxPrice != null) {
            page = productRepository.findByActiveTrueAndPriceBetween(minPrice, maxPrice, pageable);
        } else {
            page = productRepository.findByActiveTrue(pageable);
        }

        return page.map(ProductResponse::fromEntity);
    }

    public ProductResponse findById(String id) {
        return ProductResponse.fromEntity(getProductOrThrow(id));
    }

    public ProductResponse create(ProductRequest request) {
        validateCategoryExists(request.getCategoryId());

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .categoryId(request.getCategoryId())
                .imageUrl(request.getImageUrl())
                .active(true)
                .build();

        return ProductResponse.fromEntity(productRepository.save(product));
    }

    public ProductResponse update(String id, ProductRequest request) {
        validateCategoryExists(request.getCategoryId());
        Product product = getProductOrThrow(id);

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategoryId(request.getCategoryId());
        product.setImageUrl(request.getImageUrl());
        product.setUpdatedAt(Instant.now());

        return ProductResponse.fromEntity(productRepository.save(product));
    }

    /**
     * Exclusao logica (soft delete): o produto deixa de aparecer no catalogo
     * mas o historico de pedidos que ja o referenciam permanece intacto.
     */
    public void delete(String id) {
        Product product = getProductOrThrow(id);
        product.setActive(false);
        product.setUpdatedAt(Instant.now());
        productRepository.save(product);
    }

    public Product getProductOrThrow(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto nao encontrado com id: " + id));
    }

    private void validateCategoryExists(String categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Categoria nao encontrada com id: " + categoryId);
        }
    }
}
