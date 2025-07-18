package com.griddynamics.catalog.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;

@Builder
@JsonDeserialize(builder = Product.ProductBuilder.class)
public record Product(@JsonProperty("uniq_id") String uniqId, @JsonProperty("sku") String sku,
                      @JsonProperty("name_title") String nameTitle, @JsonProperty("description") String description,
                      @JsonProperty("list_price") String listPrice, @JsonProperty("sale_price") String salePrice,
                      @JsonProperty("category") String category, @JsonProperty("category_tree") String categoryTree,
                      @JsonProperty("average_product_rating") String averageProductRating,
                      @JsonProperty("product_url") String productUrl,
                      @JsonProperty("product_image_urls") String productImageUrls, @JsonProperty("brand") String brand,
                      @JsonProperty("total_number_reviews") String totalNumberReviews,
                      @JsonProperty("Reviews") String reviews) {
}
