package com.student.Dodo_Pizza_Project.mapper;

import com.student.Dodo_Pizza_Project.dto.OrderDTO;
import com.student.Dodo_Pizza_Project.entity.Order;
import com.student.Dodo_Pizza_Project.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "productIds", source = "products", qualifiedByName = "mapProductsToIds")
    OrderDTO toDTO(Order order);

    List<OrderDTO> toDTOList(List<Order> orders);

    @Named("mapProductsToIds")
    default List<Long> mapProductsToIds(List<Product> products) {
        if (products == null) return null;
        return products.stream()
                .map(Product::getId)
                .collect(Collectors.toList());
    }
}