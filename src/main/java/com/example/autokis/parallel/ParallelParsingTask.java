package com.example.autokis.parallel;

import com.example.autokis.parser.WebStoreProductParser;
import com.example.autokis.parser.model.Product;
import com.example.autokis.parser.provider.model.CategoryToProductUrl;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@AllArgsConstructor
public class ParallelParsingTask implements Callable<List<Product>> {
    private final WebStoreProductParser webStoreProductParser;
    private final List<CategoryToProductUrl> categoryToProductUrls;

    @Override
    public List<Product> call() throws Exception {
        List<Product> parsedProducts = new ArrayList<>();
        for (CategoryToProductUrl categoryToProductUrl : categoryToProductUrls) {
            parsedProducts.add(webStoreProductParser.parseSingleProduct(categoryToProductUrl.getProductUrl(),
                    categoryToProductUrl.getCategory()));
        }
        return parsedProducts;
    }
}
