package com.portfolio.ecommerce.service;

import com.portfolio.ecommerce.dto.request.AddCartItemRequest;
import com.portfolio.ecommerce.dto.response.CartResponse;
import com.portfolio.ecommerce.exception.BadRequestException;
import com.portfolio.ecommerce.model.Cart;
import com.portfolio.ecommerce.model.CartItem;
import com.portfolio.ecommerce.model.Product;
import com.portfolio.ecommerce.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;

    public CartResponse getCart(String userId) {
        return CartResponse.fromEntity(getOrCreateCart(userId));
    }

    public CartResponse addItem(String userId, AddCartItemRequest request) {
        Product product = productService.getProductOrThrow(request.getProductId());

        if (product.getStockQuantity() < request.getQuantity()) {
            throw new BadRequestException("Estoque insuficiente para o produto: " + product.getName());
        }

        Cart cart = getOrCreateCart(userId);

        cart.getItems().stream()
                .filter(item -> item.getProductId().equals(product.getId()))
                .findFirst()
                .ifPresentOrElse(
                        existingItem -> existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity()),
                        () -> cart.getItems().add(CartItem.builder()
                                .productId(product.getId())
                                .productName(product.getName())
                                .unitPrice(product.getPrice())
                                .quantity(request.getQuantity())
                                .build())
                );

        return CartResponse.fromEntity(cartRepository.save(cart));
    }

    public CartResponse updateItemQuantity(String userId, String productId, int quantity) {
        Cart cart = getOrCreateCart(userId);

        if (quantity == 0) {
            cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        } else {
            CartItem item = cart.getItems().stream()
                    .filter(i -> i.getProductId().equals(productId))
                    .findFirst()
                    .orElseThrow(() -> new BadRequestException("Este produto nao esta no carrinho"));
            item.setQuantity(quantity);
        }

        return CartResponse.fromEntity(cartRepository.save(cart));
    }

    public CartResponse removeItem(String userId, String productId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        return CartResponse.fromEntity(cartRepository.save(cart));
    }

    public void clearCart(String userId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public Cart getOrCreateCart(String userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> cartRepository.save(Cart.builder().userId(userId).build()));
    }
}
