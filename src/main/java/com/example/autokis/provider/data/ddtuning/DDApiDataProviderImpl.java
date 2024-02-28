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
    private final static int PRODUCT_LIMIT_PER_REQUEST = 1000;

    public List<Product> getAllProductsFromAPI() {
        log.info("Attempting to acquire products from DDTuning");
        List<Product> results = new ArrayList<>();
        String apiToken = apiTokenConfiguration.getDdTuningApiToken();
        int totalProductQuantity = getTotalProductQuantity(apiToken);

        for (int i = 0; i < totalProductQuantity; i += PRODUCT_LIMIT_PER_REQUEST) {
            DDResponse response;
            try {
                response = ddTuningFeignClient.getAllProducts(i, apiToken, PRODUCT_LIMIT_PER_REQUEST);
                log.debug("Response with offset %d: \n" + response);
            } catch (FeignException e) {
                log.error("Error when parsing DDTuning Api with offset %d".formatted(i), e);
                continue;
            }
            results.addAll(response.getData());

        }
        log.info("Acquired total of %d products from DDTuning".formatted(results.size()));
        return results;
    }

    private int getTotalProductQuantity(String apiToken) {
        return ddTuningFeignClient.getAllProducts(0, apiToken, 1).getTotalResults();
    }
}
