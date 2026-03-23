package com.example.product_search.infrastructure.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.example.product_search.domain.ProductDocument;

public interface ProductDocumentRepository extends ElasticsearchRepository<ProductDocument, String> {
}
