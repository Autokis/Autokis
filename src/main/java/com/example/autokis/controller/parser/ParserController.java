package com.example.autokis.controller.parser;

import com.example.autokis.parallel.service.ParallelParsingService;
import com.example.autokis.parser.WebStoreProductParser;
import com.example.autokis.parser.model.Product;
import com.example.autokis.parser.provider.CategoriesProvider;
import com.example.autokis.parser.provider.model.CategoryContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/auto-elegant/parsing")
public class ParserController {
    private final ParallelParsingService parallelParsingService;
    private final CategoriesProvider categoriesProvider;
    @Qualifier("auto-elegant")
    private final WebStoreProductParser webStoreProductParser;

    @GetMapping("/parallel")
    public List<Product> runParallelParsing() {
        try {
            List<CategoryContext> categoryContexts = categoriesProvider.provideCategoriesWithProductLinks();
            return parallelParsingService.parseProductsInParallel(categoryContexts, webStoreProductParser);
        } catch (IOException e) {
            log.error("Unable to parse auto-elegant website. Exception: ", e);
        }
        return new ArrayList<>();
    }

    @GetMapping("/default")
    public List<Product> runDefaultParsing() {
        List<Product> parsedProducts = new ArrayList<>();
        try {
            Instant start = Instant.now();
            List<CategoryContext> categoryContexts = categoriesProvider.provideCategoriesWithProductLinks();
            for (CategoryContext context : categoryContexts) {
                for (String productUrl : context.getProductLinks()) {
                    webStoreProductParser.parseSingleProduct(productUrl, context.getCategory());
                }
            }
            Instant end = Instant.now();
            System.out.printf("Parsing time: %d", Duration.between(start, end).toSeconds());
        } catch (IOException e) {
            log.error("Unable to parse auto-elegant website. Exception: ", e);
        }
        return parsedProducts;
    }
}
