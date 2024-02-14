package com.example.autokis.parallel.service;


import com.example.autokis.parallel.ParallelParsingTask;
import com.example.autokis.parser.Parser;
import com.example.autokis.parser.model.Product;
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
public class ParallelParsingService {
    //In future this field will be taken from application.properties
    private final static Integer THREAD_NUMBER = 2;

    public List<Product> parseUrlsInParallel(List<String> categoriesUrls, Parser parser) {
        try {
            List<ParallelParsingTask> taskToExecute = new ArrayList<>();
            List<Product> result = new ArrayList<>();
            int remain = categoriesUrls.size() % THREAD_NUMBER;
            int quantityToParsePerThread = categoriesUrls.size() / THREAD_NUMBER;
            int offset = 0;

            for (int i = 0; i < THREAD_NUMBER; i++) {
                List<String> urlsToParse = new ArrayList<>();
                for (int j = 0; j < quantityToParsePerThread; j++, offset++) {
                    if (i == 0) {
                        urlsToParse.add(categoriesUrls.get(j));
                        continue;
                    }
                    urlsToParse.add(categoriesUrls.get(i + offset));
                }
                if (THREAD_NUMBER - 1 == i) {
                    for (int k = 0; k < remain; k++) {
                        urlsToParse.add(categoriesUrls.get(k + offset));
                    }
                }
                taskToExecute.add(new ParallelParsingTask(parser, urlsToParse));
            }

            Instant start = Instant.now();
            ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUMBER);
            List<Future<List<Product>>> futures = executorService.invokeAll(taskToExecute);

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
}
