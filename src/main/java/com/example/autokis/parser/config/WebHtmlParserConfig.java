package com.example.autokis.parser.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class WebHtmlParserConfig {
    @Value("${web.parser.auto-elegant.url}")
    private String autoElegantUrl;
}
