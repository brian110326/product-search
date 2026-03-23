package com.example.product_search.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.product_search.domain.ProductDocument;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class BulkIndexWriter {

	private final ElasticsearchOperations elasticsearchOperations;

	// ===== Queue =====
	private final BlockingQueue<ProductDocument> queue = new LinkedBlockingQueue<>();

	private static final int BATCH_SIZE = 500;

	public void add(ProductDocument doc) {
		queue.offer(doc);
	}

	public int flush() {
		List<ProductDocument> batch = new ArrayList<>();
		queue.drainTo(batch, BATCH_SIZE);

		if (batch.isEmpty()) {
			return 0;
		}

		List<IndexQuery> queries = batch.stream()
			.map(doc -> new IndexQueryBuilder()
				.withId(doc.getId())
				.withObject(doc)
				.build())
			.toList();

		elasticsearchOperations.bulkIndex(
			queries,
			IndexCoordinates.of("products")
		);

		return batch.size();
	}

	@Scheduled(fixedDelay = 1000)
	public void autoFlush() {
		flush();
	}

}