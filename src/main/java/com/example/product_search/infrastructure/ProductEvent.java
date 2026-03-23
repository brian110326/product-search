package com.example.product_search.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductEvent {

	private Long id;
	private String name;
	private String description;
	private int price;
	private double rating;
	private String category;
}
