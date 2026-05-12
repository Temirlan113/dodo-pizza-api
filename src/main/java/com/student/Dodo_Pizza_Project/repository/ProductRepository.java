package com.student.Dodo_Pizza_Project.repository;

import com.student.Dodo_Pizza_Project.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
