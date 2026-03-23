package com.example.product_search.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchDocument {

	private Long id;
	private String name;
	private String description;
	private int price;

}
