package com.example.autokis.service;

import com.example.autokis.openapi.api.ProductsApiDelegate;
import com.example.autokis.openapi.model.Product;
import com.example.autokis.provider.ApiDataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductsApiDelegate {
    private final ApiDataProvider dataProvider;
    @Override
    public ResponseEntity<List<Product>> getAllProducts() {
        //Later would use db call
        return ResponseEntity.ok().body(dataProvider.getAllProductsFromAPI());
    }
}
