package com.student.Dodo_Pizza_Project.service;

import com.student.Dodo_Pizza_Project.entity.Category;
import com.student.Dodo_Pizza_Project.entity.Product;
import com.student.Dodo_Pizza_Project.exception.BadRequestException;
import com.student.Dodo_Pizza_Project.exception.InvalidProductPriceException;
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

import java.math.BigDecimal;
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

        Category category = null;
        if (dto.getCategoryName() != null) {
            category = categoryRepository.findByName(dto.getCategoryName())
                    .orElseThrow(() -> new ResourceNotFoundException("Категория '" + dto.getCategoryName() + "' не найдена"));
        }

        validateProductPrice(dto.getPrice(), dto.getCategoryName());

        if (category != null && productRepository.existsByNameAndCategory(dto.getName(), category)) {
            throw new BadRequestException("Продукт с названием '" + dto.getName() + "' уже существует в категории '" + dto.getCategoryName() + "'");
        }

        Product product = productMapper.toEntity(dto);
        if (category != null) {
            product.setCategory(category);
        }

        product.setActive(true);

        Product savedProduct = productRepository.save(product);
        log.info("Продукт успешно создан с ID: {}", savedProduct.getId());
        return productMapper.toDTO(savedProduct);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Продукт с id " + id + " не найден"));

        if (!product.isActive()) {
            throw new ResourceNotFoundException("Продукт с id " + id + " не найден или удален");
        }

        return productMapper.toDTO(product);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        return productRepository.findById(id).map(existingProduct -> {
            if (!existingProduct.isActive()) {
                throw new ResourceNotFoundException("Продукт с id " + id + " не найден или удален");
            }

            validateProductPrice(dto.getPrice(), dto.getCategoryName());

            existingProduct.setName(dto.getName());
            existingProduct.setPrice(dto.getPrice());

            if (dto.getCategoryName() != null) {
                Category category = categoryRepository.findByName(dto.getCategoryName())
                        .orElseThrow(() -> new ResourceNotFoundException("Категория '" + dto.getCategoryName() + "' не найдена"));
                existingProduct.setCategory(category);
            }

            Product updatedProduct = productRepository.save(existingProduct);
            return productMapper.toDTO(updatedProduct);
        }).orElseThrow(() -> new ResourceNotFoundException("Продукт с id " + id + " не найден"));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Продукт с id " + id + " не найден"));

        if (!product.isActive()) {
            throw new ResourceNotFoundException("Продукт с id " + id + " уже удален");
        }

        product.setActive(false);
        productRepository.save(product);
        log.info("Продукт с ID {} успешно переведен в статус 'удален' (Soft Delete)", id);
    }

    @Override
    public List<ProductDTO> getAllProducts(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name())
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);


        Page<Product> productPage = productRepository.findAll(pageable);

        List<Product> activeProducts = productPage.getContent().stream()
                .filter(Product::isActive)
                .toList();

        return productMapper.toDTOList(activeProducts);
    }

    // Вспомогательный приватный метод для бизнес-логики валидации цен
    private void validateProductPrice(BigDecimal price, String categoryName) {
        if (price == null || categoryName == null) return;

        if (categoryName.equalsIgnoreCase("Пицца") && price.compareTo(BigDecimal.valueOf(1500.0)) < 0) {
            throw new InvalidProductPriceException("Цена пиццы не может быть ниже 1500 тенге");
        }
        if (categoryName.equalsIgnoreCase("Соусы") && price.compareTo(BigDecimal.valueOf(500.0)) > 0) {
            throw new InvalidProductPriceException("Цена соуса не может быть выше 500 тенге");
        }
    }
}