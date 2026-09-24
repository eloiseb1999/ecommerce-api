package com.portfolio.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da aplicacao.
 *
 * API REST de um backend de e-commerce contendo:
 *  - Autenticacao e autorizacao com JWT (roles ADMIN e USER)
 *  - Catalogo de produtos e categorias
 *  - Carrinho de compras por usuario
 *  - Fechamento de pedidos (checkout) e acompanhamento de status
 */
@SpringBootApplication
public class EcommerceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceBackendApplication.class, args);
    }
}
