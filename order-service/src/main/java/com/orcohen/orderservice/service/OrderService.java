package com.orcohen.orderservice.service;

import com.orcohen.orderservice.dto.OrderLineItemsDto;
import com.orcohen.orderservice.dto.OrderRequest;
import com.orcohen.orderservice.dto.OrderResponse;
import com.orcohen.orderservice.model.Order;
import com.orcohen.orderservice.model.OrderLineItems;
import com.orcohen.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;  // injected by @RequiredArgsConstructor


    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        order.setOrderLineItemsList(orderLineItems);

        orderRepository.save(order);
        log.info("Order {} created successfully", order.getId());
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        log.info("Found {} orders", orders.size());
        return orders
                .stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());

    }

    /* ------------------------------------------ PRIVATE METHODS ------------------------------------------ */
    private OrderResponse mapToProductResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderLineItemsList(order.getOrderLineItemsList())
                .build();
    }
    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
