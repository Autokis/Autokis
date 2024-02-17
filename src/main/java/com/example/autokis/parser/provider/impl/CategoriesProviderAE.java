package com.example.autokis.parser.provider.impl;

import com.example.autokis.parser.config.WebHtmlParserConfig;
import com.example.autokis.parser.model.Category;
import com.example.autokis.parser.provider.CategoriesProvider;
import com.example.autokis.parser.provider.model.CategoryContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CategoriesProviderAE implements CategoriesProvider {

    private static final String LIMIT_POSTFIX = "&limit=200";
    private static final String PAGE_PREFIX = "?page=";

    private final WebHtmlParserConfig webHtmlParserConfig;
    private Document connectToWebsite() throws IOException {
        return Jsoup.connect(webHtmlParserConfig.getAutoElegantUrl()).get();
    }

    @Override
    public List<CategoryContext> provideCategoriesWithProductLinks() throws IOException{
        Document currentDocument = connectToWebsite();
        Elements linksToCategories = currentDocument.select("aside ul li a");
        Elements linkToSubcategories = currentDocument.select("aside ul li");
        List<Category> categories = new ArrayList<>();
        List<CategoryContext> categoryContexts = new ArrayList<>();

        for (Element e : linksToCategories) {
            String validUrl = modifyUrlIfNotFull(e.attr("href"));
            Category category = Category.builder()
                    .name(e.text())
                    .url(validUrl)
                    .build();
            categories.add(category);
        }
        removeCategoryIfItHasSubcategory(categories, linkToSubcategories);

        for (Category category : categories) {
            CategoryContext categoryContext = CategoryContext.builder()
                    .category(category)
                    .productLinks(provideProductLinks(category))
                    .build();
            categoryContexts.add(categoryContext);
        }

        return categoryContexts;
    }

    private void removeCategoryIfItHasSubcategory(List<Category> categories, Elements linksToCategories) {
        List<Element> ul = linksToCategories.stream().filter(element -> !element.select("ul").isEmpty()).toList();
        List<String> a = new ArrayList<>(ul.stream().map(element -> element.select("a").first()).map(Element::text).toList());
        a.remove(0);
        categories.removeIf(category -> a.contains(category.getName()));
    }

    private List<String> provideProductLinks(Category category) throws IOException {
        Document pageWithAllProducts = Jsoup.connect(category.getUrl() + LIMIT_POSTFIX).get();
        Element pagesString = pageWithAllProducts.getElementsByClass("results").first();
        int pagesCountFromPageString = getPagesCountFromPageString(pagesString.text());
        List<String> productLinks = new ArrayList<>();

        for (int i = 1; i < pagesCountFromPageString + 1; i++) {
            productLinks.addAll(parseSinglePage(category.getUrl() + LIMIT_POSTFIX, i));
        }
        if (productLinks.size() == 0) {
            productLinks = tryToParseLinksFromTable(pageWithAllProducts);
        }

        log.info("Acquired %d product links".formatted(productLinks.size()));
        return productLinks;
    }

    private List<String> tryToParseLinksFromTable(Document pageWithAllProducts) {
        List<String> productLinks = new ArrayList<>();
        Element table = pageWithAllProducts.getElementsByClass("search-table").first();
        table.select("a").forEach(element -> productLinks.add(element.attr("href")));
        productLinks.removeIf(String::isEmpty);
        return productLinks;
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

    private int getPagesCountFromPageString(String text) {
        String[] numbers = text.split("[^0-9]");
        return Integer.parseInt(numbers[numbers.length - 1]);
    }

    private String modifyUrlIfNotFull(String url) {
        return !url.contains(webHtmlParserConfig.getAutoElegantUrl()) ?
                webHtmlParserConfig.getAutoElegantUrl() + url : url;
    }
}
