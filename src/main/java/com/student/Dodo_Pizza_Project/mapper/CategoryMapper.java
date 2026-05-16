package com.student.Dodo_Pizza_Project.mapper;

import com.student.Dodo_Pizza_Project.entity.Category;
import com.student.Dodo_Pizza_Project.dto.CategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO toDTO(Category category);

    @Mapping(target = "description", ignore = true)
    Category toEntity(CategoryDTO dto);
}