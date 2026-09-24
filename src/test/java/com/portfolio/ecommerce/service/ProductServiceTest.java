package com.portfolio.ecommerce.service;

import com.portfolio.ecommerce.dto.request.ProductRequest;
import com.portfolio.ecommerce.dto.response.ProductResponse;
import com.portfolio.ecommerce.exception.ResourceNotFoundException;
import com.portfolio.ecommerce.model.Product;
import com.portfolio.ecommerce.repository.CategoryRepository;
import com.portfolio.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private ProductRequest request;

    @BeforeEach
    void setUp() {
        request = new ProductRequest();
        request.setName("Teclado mecanico");
        request.setDescription("Teclado mecanico RGB");
        request.setPrice(new BigDecimal("299.90"));
        request.setStockQuantity(10);
        request.setCategoryId("cat-1");
    }

    @Test
    void deveCriarProdutoQuandoCategoriaExiste() {
        when(categoryRepository.existsById("cat-1")).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            p.setId("prod-1");
            return p;
        });

        ProductResponse response = productService.create(request);

        assertThat(response.getId()).isEqualTo("prod-1");
        assertThat(response.getName()).isEqualTo("Teclado mecanico");
        assertThat(response.getPrice()).isEqualByComparingTo("299.90");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void naoDeveCriarProdutoQuandoCategoriaNaoExiste() {
        when(categoryRepository.existsById("cat-1")).thenReturn(false);

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Categoria nao encontrada");

        verify(productRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExiste() {
        when(productRepository.findById("id-inexistente")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById("id-inexistente"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Produto nao encontrado");
    }

    @Test
    void deveDesativarProdutoAoDeletar() {
        Product existing = Product.builder().id("prod-1").name("Mouse").active(true).build();
        when(productRepository.findById("prod-1")).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        productService.delete("prod-1");

        assertThat(existing.isActive()).isFalse();
        verify(productRepository).save(existing);
    }
}
