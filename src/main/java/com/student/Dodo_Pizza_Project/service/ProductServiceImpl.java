package com.student.Dodo_Pizza_Project.service;
import com.student.Dodo_Pizza_Project.entity.Category;
import com.student.Dodo_Pizza_Project.entity.Product;
import com.student.Dodo_Pizza_Project.exception.ResourceNotFoundException;
import com.student.Dodo_Pizza_Project.mapper.ProductMapper;
import com.student.Dodo_Pizza_Project.repository.CategoryRepository;
import com.student.Dodo_Pizza_Project.repository.ProductRepository;
import com.student.Dodo_Pizza_Project.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;



@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDTO createProduct(ProductDTO dto) {
        Product product = productMapper.toEntity(dto);


        if (dto.getCategoryName() != null) {

            Category category = categoryRepository.findByName(dto.getCategoryName())
                    .orElse(null);
            product.setCategory(category);
        }
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Продукт с id " + id + " не найден"));
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO dto) {

        return productRepository.findById(id).map(existingProduct -> {

            existingProduct.setName(dto.getName());
            existingProduct.setPrice(dto.getPrice());

            if (dto.getCategoryName() != null) {
                Category category = categoryRepository.findByName(dto.getCategoryName())
                        .orElse(null);
                existingProduct.setCategory(category);
            }

            Product updatedProduct = productRepository.save(existingProduct);

            return productMapper.toDTO(updatedProduct);

        }).orElse(null);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }


    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.toDTOList(products);
    }


}
