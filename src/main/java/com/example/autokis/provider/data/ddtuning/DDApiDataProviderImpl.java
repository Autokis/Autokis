package com.example.autokis.provider.data.ddtuning;

import com.example.autokis.client.feign.DDTuningFeignClient;
import com.example.autokis.openapi.model.DDResponse;
import com.example.autokis.openapi.model.Product;
import com.example.autokis.provider.ApiDataProvider;
import com.example.autokis.config.ApiTokenConfiguration;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DDApiDataProviderImpl implements ApiDataProvider {
    private final DDTuningFeignClient ddTuningFeignClient;
    private final ApiTokenConfiguration apiTokenConfiguration;

    public List<Product> getAllProductsFromAPI() {
        log.info("Attempting to acquire products from DDTuning");
        List<Product> results = new ArrayList<>();

        for (int i = 0; i < 60000; i += 10000) {
            DDResponse response;
            try {
                response = ddTuningFeignClient.getAllProducts(i, apiTokenConfiguration.getDdTuningApiToken());
            } catch (FeignException e) {
                log.error("Error when parsing DDTuning Api with offset %d".formatted(i), e);
                continue;
            }
            if (response == null) {
                continue;
            }
            results.addAll(response.getData());

        }
        log.info("Acquired total of %d products from DDTuning".formatted(results.size()));
        return results;
    }
}
