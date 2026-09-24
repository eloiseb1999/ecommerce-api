package com.portfolio.ecommerce.service;

import com.portfolio.ecommerce.dto.request.CategoryRequest;
import com.portfolio.ecommerce.dto.response.CategoryResponse;
import com.portfolio.ecommerce.exception.DuplicateResourceException;
import com.portfolio.ecommerce.exception.ResourceNotFoundException;
import com.portfolio.ecommerce.model.Category;
import com.portfolio.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::fromEntity)
                .toList();
    }

    public CategoryResponse findById(String id) {
        return CategoryResponse.fromEntity(getCategoryOrThrow(id));
    }

    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException("Ja existe uma categoria com este nome");
        }
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return CategoryResponse.fromEntity(categoryRepository.save(category));
    }

    public CategoryResponse update(String id, CategoryRequest request) {
        Category category = getCategoryOrThrow(id);
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return CategoryResponse.fromEntity(categoryRepository.save(category));
    }

    public void delete(String id) {
        Category category = getCategoryOrThrow(id);
        categoryRepository.delete(category);
    }

    private Category getCategoryOrThrow(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria nao encontrada com id: " + id));
    }
}
