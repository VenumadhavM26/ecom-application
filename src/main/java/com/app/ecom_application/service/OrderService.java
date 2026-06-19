package com.app.ecom_application.service;

import com.app.ecom_application.dto.OrderItemDTO;
import com.app.ecom_application.dto.OrderResponse;
import com.app.ecom_application.model.*;
import com.app.ecom_application.repository.OrderRepository;
import com.app.ecom_application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartService cartService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public Optional<OrderResponse> createOrder(String userId) {
        // items in the cart
        List<CartItem> cartItems= cartService.getItemsFromCart(userId);

        if(cartItems.isEmpty()){
            return Optional.empty();
        }
        // user should be there
        Optional<User> userOptional=userRepository.findById(Long.valueOf(userId));
        if(userOptional.isEmpty()){
          return Optional.empty();
        }
        User user = userOptional.get();

        // total amount calculation
        BigDecimal totalPrice = cartItems.stream().map(CartItem::getPrice).
                reduce(BigDecimal.ZERO, BigDecimal::add);

        // place order
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems= cartItems.stream().map( cartItem -> new OrderItem
                (null , cartItem.getProduct(), cartItem.getQuantity(),
                        cartItem.getPrice(), order)).toList();

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        //clear cart
        cartService.clearCart(userId);

        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems().stream().
                        map(orderItem -> new OrderItemDTO(
                                orderItem.getId(),
                                orderItem.getProduct().getId(),
                                orderItem.getQuantity(),
                                orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))
                )).toList(),
                order.getCreatedAt()
        );
    }
}
