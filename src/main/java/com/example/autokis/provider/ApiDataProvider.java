package com.example.autokis.provider;

import com.example.autokis.openapi.model.Product;

import java.util.List;

public interface ApiDataProvider {
    List<Product> getAllProductsFromAPI();
}
