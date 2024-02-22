package com.example.autokis.controller.test;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
public class TestSwaggerController {

    @GetMapping
    public String testGetMethod() {
        return "Як умру то поховайте...";
    }
}