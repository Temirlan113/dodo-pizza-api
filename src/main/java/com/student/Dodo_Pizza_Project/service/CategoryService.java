package com.student.Dodo_Pizza_Project.service;

import com.student.Dodo_Pizza_Project.entity.Category;
import com.student.Dodo_Pizza_Project.repository.CategoryRepository;
import com.student.Dodo_Pizza_Project.dto.CategoryDTO;
import com.student.Dodo_Pizza_Project.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }
}