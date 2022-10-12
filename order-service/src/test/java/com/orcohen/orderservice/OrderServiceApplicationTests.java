package com.orcohen.orderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orcohen.orderservice.dto.OrderLineItemsDto;
import com.orcohen.orderservice.dto.OrderRequest;
import com.orcohen.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@Slf4j
class OrderServiceApplicationTests {

    @Container
    private static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer(
            "mysql:8.0.23")
            .withDatabaseName("order_service")
            .withUsername("root")
            .withPassword("root");
    @Autowired
    private MockMvc mockMvc;    // injected by @RequiredArgsConstructor
    @Autowired
    private ObjectMapper objectMapper;    // injected by @RequiredArgsConstructor
    @Autowired
    private OrderRepository orderRepository;    // injected by @RequiredArgsConstructor

    @Test
    @DisplayName("Place order")
    void shouldPlaceOrder() throws Exception {
        // given
        OrderRequest orderRequest = getOrderRequest();
        String orderRequestJson = objectMapper.writeValueAsString(orderRequest);
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/order")
                        .contentType("application/json")
                        .content(orderRequestJson))
                .andExpect(status().isCreated());
        // then
        Assertions.assertEquals(1, orderRepository.findAll().size());
        // logs
        log.info("Order placed successfully");
        log.info("Orders: {}", orderRepository.findAll().stream().map(order -> {
            String orderLineItems = order.getOrderLineItemsList().stream()
                    .map(orderLineItem -> orderLineItem.getSkuCode() + " " + orderLineItem.getQuantity() + " " + orderLineItem.getPrice())
                    .collect(Collectors.joining(", "));
            return order.getOrderNumber() + " " + orderLineItems;
        }).collect(Collectors.toList()));
    }

    @Test
    @DisplayName("Get all orders")
    void shouldGetAllOrders() throws Exception {
        // given
        OrderRequest orderRequest = getOrderRequest();
        String orderRequestJson = objectMapper.writeValueAsString(orderRequest);
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/order")
                        .contentType("application/json")
                        .content(orderRequestJson))
                .andExpect(status().isCreated());
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/order"))
                .andExpect(status().isOk());
        // logs
        log.info("Orders: {}", orderRepository.findAll().stream().map(order -> {
            String orderLineItems = order.getOrderLineItemsList().stream()
                    .map(orderLineItem -> orderLineItem.getSkuCode() + " " + orderLineItem.getQuantity() + " " + orderLineItem.getPrice())
                    .collect(Collectors.joining(", "));
            return order.getOrderNumber() + " " + orderLineItems;
        }).collect(Collectors.toList()));
    }

    private OrderRequest getOrderRequest() {
        OrderLineItemsDto orderLineItemsDto = OrderLineItemsDto.builder()
                .skuCode("skuCode")
                .price(BigDecimal.valueOf(1300))
                .quantity(1)
                .build();
        return OrderRequest.builder()
                .orderLineItemsDtoList(List.of(orderLineItemsDto))
                .build();
    }

}
