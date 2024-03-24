package ua.com.autokis.domain.product.dd;

import lombok.Data;

import java.util.List;

@Data
public class DDProduct {

    private Integer id;

    private String mark;

    private String model;

    private String title;

    private String category;

    private String subcategory;

    private String manufacturer;

    private List<String> images;

    private String sku;

    private double price;

    private String currency;

    private Integer quantity;

    private String warehouse;

    private String shortTitle;

    private Parent parent;
}
