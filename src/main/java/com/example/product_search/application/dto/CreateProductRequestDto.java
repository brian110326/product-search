package com.example.product_search.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateProductRequestDto {

    private String name;
    private String description;
    private int price;
    private double rating;
    private String category;

}
