package com.example.product_search.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.product_search.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
