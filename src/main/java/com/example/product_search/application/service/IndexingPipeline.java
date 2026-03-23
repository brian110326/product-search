package com.example.product_search.application.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.product_search.domain.ProductDocument;
import com.example.product_search.infrastructure.event.ProductEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class IndexingPipeline {

	private final List<IndexingStep> steps;
	private final BulkIndexWriter bulkIndexWriter;

	public ProductDocument process(ProductEvent event) {
		ProductDocument doc = new ProductDocument();
		doc.setId(event.getId().toString());

		for (IndexingStep step : steps) {
			doc = step.process(event, doc);
		}

		bulkIndexWriter.add(doc);

		return doc;
	}

}
