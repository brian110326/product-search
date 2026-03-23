package com.example.product_search.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProductResponseDto {

	private Long id;
	private String name;
	private String description;
	private int price;
	private double rating;
	private String category;

}
