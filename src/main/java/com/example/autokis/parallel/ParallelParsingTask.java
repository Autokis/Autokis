package com.example.autokis.parallel;

import com.example.autokis.parser.Parser;
import com.example.autokis.parser.model.Product;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@AllArgsConstructor
public class ParallelParsingTask implements Callable<List<Product>> {
    private final Parser parser;
    private final List<String> categoriesUrls;

    @Override
    public List<Product> call() throws Exception {
        List<Product> parsedProducts = new ArrayList<>();
        for (String categoriesUrl : categoriesUrls) {
            parsedProducts.addAll(parser.parseGoodsFromOneUrl(categoriesUrl));
        }
        return parsedProducts;
    }
}
