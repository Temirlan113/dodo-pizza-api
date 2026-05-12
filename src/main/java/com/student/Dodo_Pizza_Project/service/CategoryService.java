package com.student.Dodo_Pizza_Project.service;

import com.student.Dodo_Pizza_Project.entity.Category;
import com.student.Dodo_Pizza_Project.repository.CategoryRepository;
import com.student.Dodo_Pizza_Project.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName() != null ? category.getName() : "Без названия");
        return dto;
    }
}
