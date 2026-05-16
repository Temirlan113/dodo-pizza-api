package com.student.Dodo_Pizza_Project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDTO {

    private Long id;

    @NotBlank(message = "Имя клиента не должно быть пустым")
    private String customerName;

    @NotBlank(message = "Адрес доставки не должен быть пустым")
    private String deliveryAddress;

    @NotEmpty(message = "Заказ должен содержать хотя бы один продукт")
    private List<Long> productIds;

    private String status;

    private BigDecimal totalPrice;
}