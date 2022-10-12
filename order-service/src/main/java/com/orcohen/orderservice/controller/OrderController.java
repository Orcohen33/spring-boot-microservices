package com.orcohen.orderservice.controller;

import com.orcohen.orderservice.dto.OrderRequest;
import com.orcohen.orderservice.dto.OrderResponse;
import com.orcohen.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService; // injected by @RequiredArgsConstructor

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest){ // <1>
        orderService.placeOrder(orderRequest);
        return "Order Placed";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getAllOrders(){
        return orderService.getAllOrders();
    }


}
