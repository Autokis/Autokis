package com.example.autokis.parser.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Category {
    private String url;
    private String name;
}
