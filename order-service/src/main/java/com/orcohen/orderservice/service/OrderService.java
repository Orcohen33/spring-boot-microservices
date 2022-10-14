package com.orcohen.orderservice.service;

import com.orcohen.orderservice.dto.InventoryResponse;
import com.orcohen.orderservice.dto.OrderLineItemsDto;
import com.orcohen.orderservice.dto.OrderRequest;
import com.orcohen.orderservice.dto.OrderResponse;
import com.orcohen.orderservice.model.Order;
import com.orcohen.orderservice.model.OrderLineItems;
import com.orcohen.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;  // injected by @RequiredArgsConstructor
    private final WebClient webClient;  // use asynchronous non-blocking requests

    /**
     * Create a new order
     * Take orderRequest from input and map it into orderLineItems
     * Initialize order.orderLineItems with orderLineItems that parsed from orderRequest
     * while also sending a request to the inventory service to reserve the items
     * and check if the items are available, if so, save the order else throw an exception
     */
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        log.info("Order toString: {}", order);

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(OrderLineItemsDto::getSkuCode).toList();

        // Call Inventory service and place order if product is in stock.
        InventoryResponse[] inventoryResponses = webClient.get()
                .uri("http://localhost:8083/api/v1/inventory/in-stock/",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build()
                )
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();// to sync the call

        boolean allInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);
        if (allInStock) {
            orderRepository.save(order);
        } else {
            log.info("Product is out of stock");
            throw new IllegalArgumentException("Product is out of stock");
        }
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
