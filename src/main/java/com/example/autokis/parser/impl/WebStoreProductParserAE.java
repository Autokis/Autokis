package com.example.autokis.parser.impl;

import com.example.autokis.parser.WebStoreProductParser;
import com.example.autokis.parser.model.Category;
import com.example.autokis.parser.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("auto-elegant")
@RequiredArgsConstructor
public class WebStoreProductParserAE implements WebStoreProductParser {
    public Product parseSingleProduct(String url, Category category) throws IOException {
        Document productDocument = Jsoup.connect(url).get();
        Element elementByClass = productDocument.getElementsByClass("product-info").first();
        Element productInfo = elementByClass.getElementsByClass("col-sm-8").first();
        Product product = Product.builder()
                .name(getProductName(productInfo))
                .availability(getProductAvailability(productInfo))
                .model(getProductModel(productInfo))
                .price(getPrice(productInfo))
                .characteristics(getAllProductCharacteristics(productDocument))
                .description(getProductDescription(productDocument))
                .imageLinks(getImageLinks(productDocument))
                .category(category)
                .build();
        log.info("Parsed new product " + product);
        return product;
    }

    private List<String> getImageLinks(Element productDocument) {
        List<String> imageLinks = new ArrayList<>();
        Element defaultGallery = productDocument.getElementById("default_gallery");
        if (defaultGallery != null) {
            defaultGallery.select("img").forEach(img ->
                    imageLinks.add(img.attr("src")));
        }
        return imageLinks;
    }

    private String getProductDescription(Element productDocument) {
        StringBuilder stringBuilder = new StringBuilder();
        Elements description = productDocument.getElementsByClass("tab-content");
        for (Element e : description) {
            if (!e.select("tbody").isEmpty()) {
                continue;
            }
            stringBuilder.append(e.text().replace("Теги", "\n Теги"));
        }
        return stringBuilder.toString();
    }

    private Map<String, String> getAllProductCharacteristics(Element productDocument) {
        Elements td = productDocument.select("td");
        Map<String, String> characteristics = new HashMap<>();

        for (int i = findCharacteristicsIndex(td) + 1; i < td.size() - 1; i = i + 2) {
            characteristics.put(td.get(i).text(), td.get(i + 1).text());
        }
        return characteristics;
    }

    private int findCharacteristicsIndex(Elements td) {
        for (int i = td.size() - 1; i >= 0; i--) {
            if (td.get(i).text().contains("Харак")) {
                return i;
            }
        }
        return td.size();
    }

    private String getProductAvailability(Element productInfo) {
        return productInfo.getElementsByClass("product-section").get(0)
                .child(3).text();
    }

    private String getProductName(Element productInfo) {
        return productInfo.select("h1").get(0).text();
    }

    private float getPrice(Element productInfo) {
        String priceString = productInfo.select("span").get(4).text().split(" ")[0];
        priceString = priceString.contains(".") ? priceString.replaceAll("\\.", "") : priceString;
        return Float.parseFloat(priceString.contains(",") ?
                priceString.replaceAll(",", "") : priceString);
    }

    private String getProductModel(Element productInfo) {
        String model = productInfo.text().split("Модель:")[1].split(" Наявність:")[0];
        return model.startsWith(" ") ? model.replaceFirst(" ", "") : model;
    }
}
