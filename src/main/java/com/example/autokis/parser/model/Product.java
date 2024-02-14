package com.example.autokis.parser.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Product {
    private String name;
    private String availability;
    private String model;
    private float price;
    private Map<String, String> characteristics;
    private String description;
    private List<String> imageLinks;
}