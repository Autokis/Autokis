package com.example.autokis.parser.impl;

import com.example.autokis.parser.Parser;
import com.example.autokis.parser.model.Product;
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

@Component("auto-elegant")
@Slf4j
public class ParserImpl implements Parser {
    private static final String WEBSITE_MAIN_URL = "https://auto-elegant.com";
    private Document currentDocument;
    private static final String PAGE_PREFIX = "?page=";

    private void connectToWebsite() throws IOException {
        currentDocument = Jsoup.connect(WEBSITE_MAIN_URL).get();
    }

    public List<String> parseCategories() throws IOException {
        connectToWebsite();
        Elements linksToCategories = currentDocument.select("aside ul li a");
        List<String> urls = new ArrayList<>();

        for (Element e : linksToCategories) {
            String validUrl = modifyUrlIfNotFull(e.attr("href"));
            urls.add(validUrl);
        }
        return urls;
    }

    public List<Product> parseGoodsFromOneUrl(String url) throws IOException {
        Document pageWithAllGoods = Jsoup.connect(url).get();
        Element pagesString = pageWithAllGoods.getElementsByClass("results").first();
        int pagesCountFromPageString = getPagesCountFromPageString(pagesString.text());
        List<String> productLinks = new ArrayList<>();
        List<Product> parsedProducts = new ArrayList<>();

        for (int i = 1; i < pagesCountFromPageString + 1; i++) {
            productLinks.addAll(parseSinglePage(url, i));
        }

        for (String link : productLinks) {
            try {
                parsedProducts.add(parseSingleProduct(link));
            } catch (IOException e) {
                log.warn("Error parsing product with link %s \n Exception: ".formatted(link), e);
            }
        }

        return parsedProducts;
    }

    public Product parseSingleProduct(String url) throws IOException {
        Document productDocument = Jsoup.connect(url).followRedirects(true).get();
        Element elementByClass = productDocument.getElementsByClass("product-info").first();
        Element productInfo = elementByClass.getElementsByClass("col-sm-8").first();
        Product product = new Product();

        product.setName(getProductName(productInfo));
        product.setAvailability(getProductAvailability(productInfo));
        product.setModel(getProductModel(productInfo));
        product.setPrice(getPrice(productInfo));
        product.setCharacteristics(getAllProductCharacteristics(productDocument));
        product.setDescription(getProductDescription(productDocument));
        product.setImageLinks(getImageLinks(productDocument));
        log.info("Parsed new product " + product);
        return product;
    }

    private List<String> parseSinglePage(String url, int pageNumber) throws IOException {
        return pageNumber == 1 ?
                parseSinglePage(url) : parseSinglePage(url + PAGE_PREFIX + pageNumber);
    }

    private List<String> parseSinglePage(String url) throws IOException {
        Document pageWithAllGoods = Jsoup.connect(url).get();
        Elements paddings = pageWithAllGoods.getElementsByClass("padding");
        List<String> productLinks = new ArrayList<>();
        for (Element element : paddings) {
            Element name = element.getElementsByClass("name").first();
            String productLink = name.getAllElements().get(1).attr("href");
            productLinks.add(productLink);
        }
        return productLinks;
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

    private int getPagesCountFromPageString(String text) {
        String[] numbers = text.split("[^0-9]");
        return Integer.parseInt(numbers[numbers.length - 1]);
    }

    private String modifyUrlIfNotFull(String url) {
        return !url.contains(WEBSITE_MAIN_URL) ? WEBSITE_MAIN_URL + url : url;
    }
}
