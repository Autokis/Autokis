package com.example.autokis.parser.provider.model;

import com.example.autokis.parser.model.Category;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryToProductUrl {
    private String productUrl;
    private Category category;
}
