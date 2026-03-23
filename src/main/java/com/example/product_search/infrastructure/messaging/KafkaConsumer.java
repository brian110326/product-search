package com.example.product_search.infrastructure.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.product_search.domain.ProductDocument;
import com.example.product_search.infrastructure.ProductEvent;
import com.example.product_search.infrastructure.repository.ProductDocumentRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

	private final ProductDocumentRepository productDocumentRepository;

	@KafkaListener(topics = "product-create", groupId = "product-group")
	public void consumeCreate(ProductEvent event) {
		ProductDocument productDocument = new ProductDocument(
			event.getId().toString(),
			event.getName(),
			event.getDescription(),
			event.getPrice(),
			event.getRating(),
			event.getCategory()
		);

		productDocumentRepository.save(productDocument);
	}

	@KafkaListener(topics = "product-delete", groupId = "product-group")
	public void consumeDelete(ProductEvent event) {
		productDocumentRepository.deleteById(event.getId().toString());
	}

}
