package com.example.product_search.infrastructure.messaging;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.product_search.application.service.IndexingPipeline;
import com.example.product_search.infrastructure.event.ProductEvent;
import com.example.product_search.infrastructure.repository.ProductDocumentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

	private final ProductDocumentRepository productDocumentRepository;
	private final IndexingPipeline indexingPipeline;

	@KafkaListener(topics = "product-create", groupId = "product-group")
	public void consume(List<ProductEvent> events) {
		for (ProductEvent event : events) {
			indexingPipeline.process(event);
		}
	}

	@KafkaListener(topics = "product-delete", groupId = "product-group")
	public void consumeDelete(ProductEvent event) {
		productDocumentRepository.deleteById(event.getId().toString());
	}

}