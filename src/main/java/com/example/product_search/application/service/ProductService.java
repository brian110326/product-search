package com.example.product_search.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.product_search.application.dto.CreateProductRequestDto;
import com.example.product_search.application.dto.ProductResponseDto;
import com.example.product_search.domain.Product;
import com.example.product_search.domain.ProductDocument;
import com.example.product_search.infrastructure.event.ProductEvent;
import com.example.product_search.infrastructure.repository.ProductDocumentRepository;
import com.example.product_search.infrastructure.repository.ProductRepository;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NumberRangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductDocumentRepository productDocumentRepository;
	private final ElasticsearchOperations elasticsearchOperations;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	@Transactional
	public ProductResponseDto createProduct(CreateProductRequestDto dto) {
		Product product = Product.builder()
			.name(dto.getName())
			.description(dto.getDescription())
			.price(dto.getPrice())
			.rating(dto.getRating())
			.category(dto.getCategory())
			.build();

		productRepository.save(product);

		ProductEvent event = new ProductEvent(
			product.getId(),
			product.getName(),
			product.getDescription(),
			product.getPrice(),
			product.getRating(),
			product.getCategory()
		);

		kafkaTemplate.send("product-create", event);

		return new ProductResponseDto(
			product.getId(),
			product.getName(), product.getDescription(), product.getPrice(),
			product.getRating(), product.getCategory()
		);
	}

	public List<String> getSuggestions(String query) {
		Query multiMatchQuery = MultiMatchQuery.of(
				m -> m.query(query)
					.type(TextQueryType.BoolPrefix)
					.fields("name.auto_complete", "name.auto_complete._2gram",
						"name.auto_complete._3gram")
			)
			._toQuery();

		NativeQuery nativeQuery = NativeQuery.builder()
			.withQuery(multiMatchQuery)
			.withPageable(PageRequest.of(0, 5))
			.build();

		SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(nativeQuery, ProductDocument.class);

		return searchHits.getSearchHits().stream()
			.map(hit -> {
				ProductDocument document = hit.getContent();
				return document.getName();
			})
			.toList();
	}

	public List<ProductResponseDto> getProducts(int page, int size) {
		Pageable pageable = PageRequest.of(page - 1, size);
		return productRepository.findAll(pageable).getContent()
			.stream().map(
				p -> new ProductResponseDto(
					p.getId(),
					p.getName(),
					p.getDescription(),
					p.getPrice(),
					p.getRating(),
					p.getCategory()
				)
			).toList();
	}

	@Transactional
	public void deleteProduct(Long id) {
		Product product = productRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("상품 없음"));

		ProductEvent event = new ProductEvent(
			product.getId(),
			product.getName(),
			product.getDescription(),
			product.getPrice(),
			product.getRating(),
			product.getCategory()
		);

		kafkaTemplate.send("product-delete", event);

		productRepository.deleteById(id);
	}

	public List<ProductDocument> searchProducts(String query, String category, double minPrice, double maxPrice,
		int page, int size) {
		Query multiMatchQuery = MultiMatchQuery.of(
				m -> m.query(query)
					.fields("name^3", "description^1", "category^2")
					.fuzziness("AUTO")
			)
			._toQuery();

		List<Query> filters = new ArrayList<>();
		if (category != null && !category.isEmpty()) {
			Query categoryFilter = TermQuery.of(
					t -> t.field("category.raw")
						.value(category)
				)
				._toQuery();
			filters.add(categoryFilter);
		}

		Query priceRangeFilter = NumberRangeQuery.of(
				r -> r.field("price")
					.gte(minPrice)
					.lte(maxPrice)
			)
			._toRangeQuery()._toQuery();
		filters.add(priceRangeFilter);

		Query ratingShould = NumberRangeQuery.of(
				r -> r.field("rating")
					.gt(4.0)
			)
			._toRangeQuery()._toQuery();

		Query boolQuery = BoolQuery.of(
				b -> b.must(multiMatchQuery)
					.filter(filters)
					.should(ratingShould)
			)
			._toQuery();

		HighlightParameters highlightParameters = HighlightParameters.builder()
			.withPreTags("<b>")
			.withPostTags("</b>")
			.build();

		Highlight highlight = new Highlight(highlightParameters, List.of(new HighlightField("name")));

		HighlightQuery highlightQuery = new HighlightQuery(highlight, ProductDocument.class);

		NativeQuery nativeQuery = NativeQuery.builder()
			.withQuery(boolQuery)
			.withHighlightQuery(highlightQuery)
			.withPageable(PageRequest.of(page - 1, size))
			.build();

		SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(
			nativeQuery, ProductDocument.class
		);

		return searchHits.getSearchHits().stream()
			.map(hit -> {
				ProductDocument productDocument = hit.getContent();
				List<String> highlightField = hit.getHighlightField("name");
				if (highlightField != null && !highlightField.isEmpty()) {
					productDocument.setName(highlightField.get(0));
				}
				return productDocument;
			})
			.toList();
	}
}
