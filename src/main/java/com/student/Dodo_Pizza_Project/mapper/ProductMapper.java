package com.student.Dodo_Pizza_Project.mapper;

import com.student.Dodo_Pizza_Project.entity.Product;
import com.student.Dodo_Pizza_Project.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categoryName", source = "category.name")
    ProductDTO toDTO(Product product);

    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductDTO dto);

    List<ProductDTO> toDTOList(List<Product> products);

}
