package com.example.autokis.parser;

import com.example.autokis.parser.model.Product;

import java.io.IOException;
import java.util.List;

public interface Parser {
    List<Product> parseGoodsFromOneUrl(String url) throws IOException;
    List<String> parseCategories() throws IOException;
}
