package com.student.Dodo_Pizza_Project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;


@Data
@Schema(description = "Информация о продукте")
public class ProductDTO {

    @Schema(description = "Уникальный идентификатор", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Название пиццы", example = "Маргарита")
    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Schema(description = "Цена в тенге", example = "2500")
    @NotNull(message = "Цена должна быть указана")
    @Positive(message = "Цена должна быть больше нуля")
    private BigDecimal price;

    @Schema(description = "Категория продукта", example = "Пицца")
    private String categoryName;
}
