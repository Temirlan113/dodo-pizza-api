package com.student.Dodo_Pizza_Project.controller;

import com.student.Dodo_Pizza_Project.dto.ProductDTO;
import com.student.Dodo_Pizza_Project.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Продукты", description = "Управление ассортиментом пиццерии")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Получить все продукты", description = "Возвращает список всех доступных товаров с их категориями")
    public List<ProductDTO> getAll() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Найти продукт по ID", description = "Возвращает данные одного продукта. Если ID не существует, бросает 404.")
    public ProductDTO getById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать новый продукт", description = "Добавляет продукт в базу. Категория привязывается по названию.")
    public ProductDTO create(@Valid @RequestBody ProductDTO dto) {
        return productService.createProduct(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить продукт", description = "Полное обновление данных продукта по его ID")
    public ProductDTO update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        return productService.updateProduct(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить продукт", description = "Удаляет запись о продукте из базы данных")
    public void delete(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}