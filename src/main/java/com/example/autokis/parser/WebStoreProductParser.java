package com.example.autokis.parser;

import com.example.autokis.parser.model.Category;
import com.example.autokis.parser.model.Product;

import java.io.IOException;

public interface WebStoreProductParser {
    Product parseSingleProduct(String url, Category category) throws IOException;
}
