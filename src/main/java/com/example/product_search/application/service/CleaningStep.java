package com.example.product_search.application.service;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.product_search.domain.ProductDocument;
import com.example.product_search.infrastructure.event.ProductEvent;

@Component
@Order(2)
public class CleaningStep implements IndexingStep {

	@Override
	public ProductDocument process(ProductEvent event, ProductDocument doc) {
		doc.setName(event.getName().trim());
		doc.setDescription(event.getDescription() == null ? "" : event.getDescription().trim());
		doc.setPrice(event.getPrice());
		doc.setCategory(event.getCategory());
		doc.setRating(event.getRating());

		return doc;
	}

}
