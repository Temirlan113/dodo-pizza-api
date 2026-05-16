package com.student.Dodo_Pizza_Project.service;

import com.student.Dodo_Pizza_Project.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(OrderDTO dto);

    List<OrderDTO> getAllOrders();
}