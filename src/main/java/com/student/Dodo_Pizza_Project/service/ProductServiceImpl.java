package com.student.Dodo_Pizza_Project.service;

import com.student.Dodo_Pizza_Project.entity.Category;
import com.student.Dodo_Pizza_Project.entity.Product;
import com.student.Dodo_Pizza_Project.exception.ResourceNotFoundException;
import com.student.Dodo_Pizza_Project.mapper.ProductMapper;
import com.student.Dodo_Pizza_Project.repository.CategoryRepository;
import com.student.Dodo_Pizza_Project.repository.ProductRepository;
import com.student.Dodo_Pizza_Project.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO dto) {
        log.info("Запрос на создание нового продукта: {}", dto.getName());
        Product product = productMapper.toEntity(dto);

        if (dto.getCategoryName() != null) {
            Category category = categoryRepository.findByName(dto.getCategoryName())
                    .orElseThrow(() -> new ResourceNotFoundException("Категория '" + dto.getCategoryName() + "' не найдена"));
            product.setCategory(category);
        }
        Product savedProduct = productRepository.save(product);
        log.info("Продукт успешно создан с ID: {}", savedProduct.getId());
        return productMapper.toDTO(savedProduct);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Продукт с id " + id + " не найден"));
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(dto.getName());
            existingProduct.setPrice(dto.getPrice());

            if (dto.getCategoryName() != null) {
                Category category = categoryRepository.findByName(dto.getCategoryName())
                        .orElseThrow(() -> new ResourceNotFoundException("Категория '" + dto.getCategoryName() + "' не найдена"));
                existingProduct.setCategory(category);
            }

            Product updatedProduct = productRepository.save(existingProduct);
            return productMapper.toDTO(updatedProduct);
        }).orElseThrow(() -> new ResourceNotFoundException("Продукт с id " + id + " не найден")); // Вместо null кидаем ошибку
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        // Проверяем существование перед удалением, чтобы не поймать 500 ошибку
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Продукт с id " + id + " не найден");
        }
        productRepository.deleteById(id);
    }


    @Override
    public List<ProductDTO> getAllProducts(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Product> productPage = productRepository.findAll(pageable);

        return productMapper.toDTOList(productPage.getContent());
    }

    @Override
    public List<ProductDTO> getAllProducts(int page, int size, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name())
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage = productRepository.findAll(pageable);

        return productMapper.toDTOList(productPage.getContent());
    }
}