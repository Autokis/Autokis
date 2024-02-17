package com.example.autokis.parallel.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ParallelParsingConfig {
    @Value("${parallel.parsing.thread-number}")
    private String threadNumber;
}
