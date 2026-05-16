package com.student.Dodo_Pizza_Project.service;

import com.student.Dodo_Pizza_Project.dto.OrderDTO;
import com.student.Dodo_Pizza_Project.entity.Order;
import com.student.Dodo_Pizza_Project.entity.OrderStatus;
import com.student.Dodo_Pizza_Project.entity.Product;
import com.student.Dodo_Pizza_Project.exception.BadRequestException;
import com.student.Dodo_Pizza_Project.exception.ResourceNotFoundException;
import com.student.Dodo_Pizza_Project.mapper.OrderMapper;
import com.student.Dodo_Pizza_Project.repository.OrderRepository;
import com.student.Dodo_Pizza_Project.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO dto) {
        log.info("Начало формирования заказа для клиента: {}", dto.getCustomerName());

        if (dto.getProductIds() == null || dto.getProductIds().isEmpty()) {
            throw new BadRequestException("Нельзя создать заказ без продуктов");
        }

        List<Product> productsToOrder = new ArrayList<>();
        BigDecimal productsSum = BigDecimal.ZERO;

        // Собираем продукты из базы и валидируем их
        for (Long productId : dto.getProductIds()) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Продукт с ID " + productId + " не найден"));

            // Бизнес-логика: нельзя купить удаленный (неактивный) товар
            if (!product.isActive()) {
                throw new BadRequestException("Продукт '" + product.getName() + "' недоступен для заказа (удален)");
            }

            productsToOrder.add(product);
            productsSum = productsSum.add(product.getPrice());
        }

        // Бизнес-логика стоимости доставки:
        // Если сумма продуктов меньше 5000 тенге, добавляем 700 тенге за доставку
        BigDecimal finalPrice = productsSum;
        if (productsSum.compareTo(BigDecimal.valueOf(5000.0)) < 0) {
            log.info("Сумма заказа ({}) ниже 5000. Добавлена стоимость доставки 700 тенге.", productsSum);
            finalPrice = finalPrice.add(BigDecimal.valueOf(700.0));
        } else {
            log.info("Бесплатная доставка для заказа от 5000 тенге.");
        }

        Order order = new Order();
        order.setCustomerName(dto.getCustomerName());
        order.setDeliveryAddress(dto.getDeliveryAddress());
        order.setProducts(productsToOrder);
        order.setTotalPrice(finalPrice);
        order.setStatus(OrderStatus.CREATED);

        Order savedOrder = orderRepository.save(order);
        log.info("Заказ №{} успешно сохранен. Итоговая сумма: {}", savedOrder.getId(), savedOrder.getTotalPrice());

        OrderDTO responseDTO = new OrderDTO();
        responseDTO.setId(savedOrder.getId());
        responseDTO.setCustomerName(savedOrder.getCustomerName());
        responseDTO.setDeliveryAddress(savedOrder.getDeliveryAddress());
        responseDTO.setTotalPrice(savedOrder.getTotalPrice());
        responseDTO.setStatus(savedOrder.getStatus().name());
        responseDTO.setProductIds(dto.getProductIds());

        return responseDTO;
    }


    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        log.info("Запрос на получение всех заказов из базы данных");
        List<Order> orders = orderRepository.findAll();


        return orderMapper.toDTOList(orders);
    }
}