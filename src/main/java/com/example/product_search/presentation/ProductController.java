package com.example.product_search.presentation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.product_search.application.ProductService;
import com.example.product_search.application.dto.CreateProductRequestDto;
import com.example.product_search.application.dto.ProductResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@PostMapping
	public ResponseEntity<ProductResponseDto> createProduct(@RequestBody CreateProductRequestDto dto) {
		ProductResponseDto responseDto = productService.createProduct(dto);
		return ResponseEntity.ok(responseDto);
	}

	@GetMapping
	public ResponseEntity<List<ProductResponseDto>> getProducts(@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size) {
		List<ProductResponseDto> list = productService.getProducts(page, size);
		return ResponseEntity.ok(list);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}

}
