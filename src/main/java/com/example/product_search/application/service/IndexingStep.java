package com.example.product_search.application.service;

import com.example.product_search.domain.ProductDocument;
import com.example.product_search.infrastructure.event.ProductEvent;

public interface IndexingStep {

	ProductDocument process(ProductEvent event, ProductDocument doc);

	default String getStepName() {
		return this.getClass().getSimpleName();
	}

}
