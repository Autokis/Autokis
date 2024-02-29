package ua.com.autokis.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.com.autokis.application.provider.ApiDataProvider;
import ua.com.autokis.openapi.api.ProductsApiDelegate;
import ua.com.autokis.openapi.model.Product;

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
