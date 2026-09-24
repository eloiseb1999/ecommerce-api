package com.portfolio.ecommerce.service;

import com.portfolio.ecommerce.dto.request.CheckoutRequest;
import com.portfolio.ecommerce.dto.response.OrderResponse;
import com.portfolio.ecommerce.exception.BadRequestException;
import com.portfolio.ecommerce.exception.ResourceNotFoundException;
import com.portfolio.ecommerce.model.*;
import com.portfolio.ecommerce.repository.OrderRepository;
import com.portfolio.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    /**
     * Fecha o pedido a partir do carrinho atual do usuario: valida estoque,
     * debita a quantidade de cada produto, cria o pedido e esvazia o carrinho.
     *
     * Nota: nao usamos @Transactional aqui porque transacoes multi-documento
     * do MongoDB exigem um replica set (o docker-compose deste projeto sobe
     * uma instancia standalone). Para producao, o caminho seria configurar
     * o Mongo como replica set e adicionar um bean MongoTransactionManager.
     */
    public OrderResponse checkout(String userId, CheckoutRequest request) {
        Cart cart = cartService.getOrCreateCart(userId);

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Nao e possivel fechar um pedido com o carrinho vazio");
        }

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> {
                    Product product = productRepository.findById(cartItem.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Produto nao encontrado com id: " + cartItem.getProductId()));

                    if (product.getStockQuantity() < cartItem.getQuantity()) {
                        throw new BadRequestException("Estoque insuficiente para o produto: " + product.getName());
                    }

                    product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
                    productRepository.save(product);

                    return OrderItem.builder()
                            .productId(cartItem.getProductId())
                            .productName(cartItem.getProductName())
                            .unitPrice(cartItem.getUnitPrice())
                            .quantity(cartItem.getQuantity())
                            .build();
                })
                .toList();

        BigDecimal total = orderItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .userId(userId)
                .items(orderItems)
                .totalAmount(total)
                .status(OrderStatus.PENDING)
                .shippingAddress(request.getShippingAddress())
                .build();

        order = orderRepository.save(order);
        cartService.clearCart(userId);

        return OrderResponse.fromEntity(order);
    }

    public Page<OrderResponse> findByUser(String userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable).map(OrderResponse::fromEntity);
    }

    public Page<OrderResponse> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable).map(OrderResponse::fromEntity);
    }

    public OrderResponse findById(String id) {
        return OrderResponse.fromEntity(getOrderOrThrow(id));
    }

    /**
     * Garante que um usuario comum so acesse os proprios pedidos; admins podem ver qualquer um.
     */
    public OrderResponse findByIdForUser(String id, String userId) {
        Order order = getOrderOrThrow(id);
        if (!order.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("Pedido nao encontrado com id: " + id);
        }
        return OrderResponse.fromEntity(order);
    }

    public OrderResponse updateStatus(String id, OrderStatus status) {
        Order order = getOrderOrThrow(id);
        order.setStatus(status);
        order.setUpdatedAt(Instant.now());
        return OrderResponse.fromEntity(orderRepository.save(order));
    }

    private Order getOrderOrThrow(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido nao encontrado com id: " + id));
    }
}
