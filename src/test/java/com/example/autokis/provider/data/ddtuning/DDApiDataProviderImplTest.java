package com.example.autokis.provider.data.ddtuning;

import com.example.autokis.client.feign.DDTuningFeignClient;
import com.example.autokis.config.ApiTokenConfiguration;
import com.example.autokis.openapi.model.DDResponse;
import com.example.autokis.openapi.model.Product;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class DDApiDataProviderImplTest {
    @InjectMocks
    private DDApiDataProviderImpl dataProvider;
    @Mock
    private DDTuningFeignClient ddTuningFeignClient;
    @Mock
    private ApiTokenConfiguration apiTokenConfiguration;
    private static final int PRODUCTS_PER_REQUEST = 1000;

    @BeforeEach
    public void setUp() {
        when(apiTokenConfiguration.getDdTuningApiToken()).thenReturn("test");
    }

    @Test
    void getAllProductsFromAPI() {
        DDResponse response = prepareResponse();

        when(ddTuningFeignClient.getAllProducts(anyInt(), eq("test"), anyInt())).thenReturn(response);

        List<Product> allProductsFromAPI = dataProvider.getAllProductsFromAPI();
        assertEquals(response.getTotalResults() / PRODUCTS_PER_REQUEST, allProductsFromAPI.size());
    }

    @Test
    void getAllProductsFromApiFeignClientThrowsException() {
        DDResponse response = prepareResponse();
        when(ddTuningFeignClient.getAllProducts(0, "test", 1)).thenReturn(response);
        when(ddTuningFeignClient.getAllProducts(anyInt(), eq("test"), eq(1000))).thenThrow(FeignException.NotFound.class);

        List<Product> allProductsFromAPI = dataProvider.getAllProductsFromAPI();
        assertEquals(0, allProductsFromAPI.size());
    }

    private DDResponse prepareResponse() {
        DDResponse response = new DDResponse();
        Product product = new Product();
        product.setId(1);
        product.setCategory("test");
        product.setTitle("test");
        response.setTotalResults(60000);
        response.data(List.of(product));
        return response;
    }
}