package com.example.autokis.parser.provider.model;

import com.example.autokis.parser.model.Category;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CategoryContext {
    private Category category;
    private List<String> productLinks;
}
