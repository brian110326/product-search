package com.example.product_search.domain;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.annotations.Setting;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(indexName = "products")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Setting(settingPath = "/elasticsearch/product-settings.json")
public class ProductDocument {

	@Id
	private String id;

	@MultiField(mainField = @Field(type = FieldType.Text, analyzer = "product_name_analyzer"),
		otherFields = {
			@InnerField(suffix = "auto_complete", type = FieldType.Search_As_You_Type, analyzer = "nori")
		})
	private String name;

	@Field(type = FieldType.Text, analyzer = "product_description_analyzer")
	private String description;

	@Field(type = FieldType.Integer)
	private Integer price;

	@Field(type = FieldType.Double)
	private Double rating;

	@MultiField(mainField = @Field(type = FieldType.Text, analyzer = "product_category_analyzer"),
		otherFields = {
			@InnerField(suffix = "raw", type = FieldType.Keyword)
		})
	private String category;

}
