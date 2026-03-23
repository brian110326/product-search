package com.example.product_search.application;

import com.example.product_search.application.dto.CreateProductRequestDto;
import com.example.product_search.application.dto.ProductResponseDto;
import com.example.product_search.domain.Product;
import com.example.product_search.infrastructure.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponseDto createProduct(CreateProductRequestDto dto){
        Product product = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .rating(dto.getRating())
                .category(dto.getCategory())
                .build();

        productRepository.save(product);

        return new ProductResponseDto(
                product.getId(),
                product.getName(), product.getDescription(), product.getPrice(),
                product.getRating(), product.getCategory()
        );
    }

    public List<ProductResponseDto> getProducts(int page, int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        return productRepository.findAll(pageable).getContent()
                .stream().map(
                        p -> new ProductResponseDto(
                                p.getId(),
                                p.getName(),
                                p.getDescription(),
                                p.getPrice(),
                                p.getRating(),
                                p.getCategory()
                        )
                ).toList();
    }

    @Transactional
    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }

}
