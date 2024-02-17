package com.example.autokis.parser.provider;

import com.example.autokis.parser.provider.model.CategoryContext;

import java.io.IOException;
import java.util.List;

public interface CategoriesProvider {
    List<CategoryContext> provideCategoriesWithProductLinks() throws IOException;
}
