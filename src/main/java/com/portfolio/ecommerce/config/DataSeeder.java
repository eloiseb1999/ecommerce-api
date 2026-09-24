package com.portfolio.ecommerce.config;

import com.portfolio.ecommerce.model.Category;
import com.portfolio.ecommerce.model.Role;
import com.portfolio.ecommerce.model.User;
import com.portfolio.ecommerce.repository.CategoryRepository;
import com.portfolio.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Cria um usuario ADMIN padrao e algumas categorias de exemplo na primeira
 * execucao, para facilitar testes locais e demonstracoes da API.
 * Pode ser desativado com a propriedade app.seed-data=false.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed-data:true}")
    private boolean seedData;

    @Override
    public void run(String... args) {
        if (!seedData) {
            return;
        }

        if (!userRepository.existsByEmail("admin@ecommerce.com")) {
            User admin = User.builder()
                    .name("Administrador")
                    .email("admin@ecommerce.com")
                    .password(passwordEncoder.encode("admin123"))
                    .roles(Set.of(Role.ROLE_ADMIN, Role.ROLE_USER))
                    .build();
            userRepository.save(admin);
            log.info(">> Usuario admin criado: admin@ecommerce.com / admin123");
        }

        seedCategory("Eletronicos", "Celulares, notebooks e acessorios em geral");
        seedCategory("Livros", "Livros fisicos e digitais de todos os generos");
        seedCategory("Roupas", "Vestuario masculino e feminino");
    }

    private void seedCategory(String name, String description) {
        if (!categoryRepository.existsByNameIgnoreCase(name)) {
            categoryRepository.save(Category.builder().name(name).description(description).build());
            log.info(">> Categoria de exemplo criada: {}", name);
        }
    }
}
