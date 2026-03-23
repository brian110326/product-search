package com.example.product_search.application.service;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.product_search.domain.ProductDocument;
import com.example.product_search.infrastructure.event.ProductEvent;

@Component
@Order(1)
public class ValidationStep implements IndexingStep {

	@Override
	public ProductDocument process(ProductEvent event, ProductDocument doc) {
		if (event.getName() == null || event.getName().isBlank()) {
			throw new IllegalArgumentException("Name is required");
		}

		return doc;
	}

}
