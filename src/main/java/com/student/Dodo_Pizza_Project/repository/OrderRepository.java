package com.student.Dodo_Pizza_Project.repository;

import com.student.Dodo_Pizza_Project.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
