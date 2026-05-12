package com.student.Dodo_Pizza_Project.service;

import com.student.Dodo_Pizza_Project.dto.ProductDTO;

import java.util.List;

public interface ProductService {
        ProductDTO createProduct(ProductDTO dto);
        ProductDTO getProductById(Long id);
        List<ProductDTO> getAllProducts();
        ProductDTO updateProduct(Long id, ProductDTO dto);
        void deleteProduct(Long id);

}
