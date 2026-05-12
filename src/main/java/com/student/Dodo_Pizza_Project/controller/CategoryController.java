package com.student.Dodo_Pizza_Project.controller;

import com.student.Dodo_Pizza_Project.service.CategoryService;
import com.student.Dodo_Pizza_Project.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDTO> getAllCategories() {

        return categoryService.getAllCategories();

    }
}
