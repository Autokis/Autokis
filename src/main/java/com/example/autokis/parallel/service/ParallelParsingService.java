package com.example.autokis.parallel.service;


import com.example.autokis.parallel.ParallelParsingTask;
import com.example.autokis.parallel.config.ParallelParsingConfig;
import com.example.autokis.parser.WebStoreProductParser;
import com.example.autokis.parser.model.Category;
import com.example.autokis.parser.model.Product;
import com.example.autokis.parser.provider.model.CategoryContext;
import com.example.autokis.parser.provider.model.CategoryToProductUrl;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@AllArgsConstructor
public class ParallelParsingService {
    private final ParallelParsingConfig parallelParsingConfig;

    public List<Product> parseProductsInParallel(List<CategoryContext> categoryContexts, WebStoreProductParser webStoreProductParser) {
        try {
            int threadNumber = Integer.parseInt(parallelParsingConfig.getThreadNumber());
            List<ParallelParsingTask> tasksToExecute = new ArrayList<>();
            List<CategoryToProductUrl> categoryToProductUrls = getProductToCategoryMappingFromContext(categoryContexts);
            List<List<CategoryToProductUrl>> threadPartitions = Lists.partition(categoryToProductUrls, threadNumber);
            List<Product> result = new ArrayList<>();

            for (List<CategoryToProductUrl> partition : threadPartitions) {
                tasksToExecute.add(new ParallelParsingTask(webStoreProductParser, partition));
            }

            Instant start = Instant.now();
            ExecutorService executorService = Executors.newFixedThreadPool(threadNumber);
            List<Future<List<Product>>> futures = executorService.invokeAll(tasksToExecute);

            for (Future<List<Product>> future : futures) {
                result.addAll(future.get());
            }

            Instant end = Instant.now();
            executorService.shutdown();
            System.out.printf("Parsing time: %d", Duration.between(start, end).toSeconds());
            return result;

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private List<CategoryToProductUrl> getProductToCategoryMappingFromContext(List<CategoryContext> categoryContexts) {
        List<CategoryToProductUrl> categoryToProductUrls = new ArrayList<>();
        for (CategoryContext categoryContext : categoryContexts) {
            for (String productUrl : categoryContext.getProductLinks()) {
                CategoryToProductUrl categoryToProductUrl = CategoryToProductUrl.builder()
                        .category(categoryContext.getCategory())
                        .productUrl(productUrl)
                        .build();
                categoryToProductUrls.add(categoryToProductUrl);
            }
        }
        return categoryToProductUrls;
    }
}
