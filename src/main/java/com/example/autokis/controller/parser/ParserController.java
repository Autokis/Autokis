package com.example.autokis.controller.parser;

import com.example.autokis.parallel.service.ParallelParsingService;
import com.example.autokis.parser.Parser;
import com.example.autokis.parser.model.Product;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/auto-elegant/parsing")
@Slf4j
public class ParserController {
    private final ParallelParsingService parallelParsingService;
    @Qualifier("auto-elegant")
    private final Parser parser;

    @GetMapping("/parallel")
    public List<Product> runParallelParsing() {
        try {
            List<String> urls = parser.parseCategories();
            return parallelParsingService.parseUrlsInParallel(urls, parser);
        } catch (IOException e) {
            log.error("Unable to parse auto-elegant website. Exception: ", e);
        }
        return new ArrayList<>();
    }

    @GetMapping("/default")
    public List<Product> runDefaultParsing() {
        List<Product> parsedProducts = new ArrayList<>();
        try {
            List<String> urls = parser.parseCategories();
            for (String url : urls) {
                parsedProducts.addAll(parser.parseGoodsFromOneUrl(url));
            }
        } catch (IOException e) {
            log.error("Unable to parse auto-elegant website. Exception: ", e);
        }
        return parsedProducts;
    }
}
