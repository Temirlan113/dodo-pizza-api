package com.student.Dodo_Pizza_Project.service;

import com.student.Dodo_Pizza_Project.dto.ProductDTO;
import com.student.Dodo_Pizza_Project.entity.Category;
import com.student.Dodo_Pizza_Project.entity.Product;
import com.student.Dodo_Pizza_Project.exception.BadRequestException;
import com.student.Dodo_Pizza_Project.exception.InvalidProductPriceException;
import com.student.Dodo_Pizza_Project.exception.ResourceNotFoundException;
import com.student.Dodo_Pizza_Project.mapper.ProductMapper;
import com.student.Dodo_Pizza_Project.repository.CategoryRepository;
import com.student.Dodo_Pizza_Project.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductDTO productDTO;
    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        productDTO = new ProductDTO();
        productDTO.setName("Пепперони");
        productDTO.setPrice(BigDecimal.valueOf(2500.0));
        productDTO.setCategoryName("Пицца");

        category = new Category();
        category.setId(1L);
        category.setName("Пицца");

        product = new Product();
        product.setId(1L);
        product.setName("Пепперони");
        product.setPrice(BigDecimal.valueOf(2500.0));
        product.setCategory(category);
        product.setActive(true);
    }

    @Test
    void createProduct_Success() {
        // Arrange
        when(categoryRepository.findByName("Пицца")).thenReturn(Optional.of(category));
        when(productRepository.existsByNameAndCategory("Пепперони", category)).thenReturn(false);
        when(productMapper.toEntity(productDTO)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDTO(product)).thenReturn(productDTO);

        // Act
        ProductDTO result = productService.createProduct(productDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Пепперони", result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void createProduct_ThrowsException_WhenCategoryNotFound() {
        // Arrange
        when(categoryRepository.findByName("Пицца")).thenReturn(Optional.empty());
        // Никаких лишних stubbing-ов! Код упадет сразу здесь.

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> productService.createProduct(productDTO));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void createProduct_ThrowsException_WhenPriceTooLowForPizza() {
        // Arrange
        productDTO.setPrice(BigDecimal.valueOf(1000.0)); // Цена ниже 1500 для Пиццы
        when(categoryRepository.findByName("Пицца")).thenReturn(Optional.of(category));

        // Act & Assert
        assertThrows(InvalidProductPriceException.class, () -> productService.createProduct(productDTO));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void createProduct_ThrowsException_WhenDuplicateNameInCategory() {
        // Arrange
        when(categoryRepository.findByName("Пицца")).thenReturn(Optional.of(category));
        // Имитируем, что такой продукт уже есть в этой категории
        when(productRepository.existsByNameAndCategory("Пепперони", category)).thenReturn(true);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> productService.createProduct(productDTO));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void getProductById_Success() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDTO(product)).thenReturn(productDTO);

        // Act
        ProductDTO result = productService.getProductById(1L);

        // Assert
        assertNotNull(result);
        assertTrue(BigDecimal.valueOf(2500.0).compareTo(result.getPrice()) == 0);
    }

    @Test
    void getProductById_ThrowsException_WhenProductIsDeleted() {
        // Arrange
        product.setActive(false); // Имитируем Soft Delete
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    void deleteProduct_SoftDelete_Success() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        productService.deleteProduct(1L);

        // Assert
        assertFalse(product.isActive()); // Проверяем, что флаг сменился на false
        verify(productRepository, times(1)).save(product); // Проверяем, что состояние сохранилось
    }

    @Test
    void getAllProducts_WithSorting_Success() {
        // Arrange
        int page = 0;
        int size = 10;
        String sortBy = "price";
        String sortDir = "desc";

        Page<Product> productPage = mock(Page.class);
        List<Product> productList = List.of(product);
        List<ProductDTO> dtoList = List.of(productDTO);

        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);
        when(productPage.getContent()).thenReturn(productList);
        when(productMapper.toDTOList(anyList())).thenReturn(dtoList);

        // Act
        List<ProductDTO> result = productService.getAllProducts(page, size, sortBy, sortDir);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1)).findAll(any(Pageable.class));
    }
}