package com.student.Dodo_Pizza_Project.controller;

import com.student.Dodo_Pizza_Project.dto.OrderDTO;
import com.student.Dodo_Pizza_Project.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO dto) {
        log.info("Получен HTTP-запрос на создание заказа для: {}", dto.getCustomerName());
        OrderDTO createdOrder = orderService.createOrder(dto);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping
    public List<OrderDTO> getAll() {
        log.info("Получен HTTP-запрос на получение списка всех заказов");
        return orderService.getAllOrders();
    }
}