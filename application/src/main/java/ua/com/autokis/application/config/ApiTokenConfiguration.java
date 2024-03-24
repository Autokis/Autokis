package ua.com.autokis.application.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ApiTokenConfiguration {
    @Value("${api.token.dd}")
    private String ddTuningApiToken;
}
