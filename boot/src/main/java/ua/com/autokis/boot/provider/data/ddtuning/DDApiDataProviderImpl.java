package ua.com.autokis.boot.provider.data.ddtuning;

import ua.com.autokis.api.сlient.feign.DDTuningFeignClient;
import ua.com.autokis.boot.config.ApiTokenConfiguration;
import ua.com.autokis.boot.provider.ApiDataProvider;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.com.autokis.openapi.model.DDResponse;
import ua.com.autokis.openapi.model.Product;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DDApiDataProviderImpl implements ApiDataProvider {
    private final DDTuningFeignClient ddTuningFeignClient;
    private final ApiTokenConfiguration apiTokenConfiguration;
    private static final int PRODUCT_LIMIT_PER_REQUEST = 1000;

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
