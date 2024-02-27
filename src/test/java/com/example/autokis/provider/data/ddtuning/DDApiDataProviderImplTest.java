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

    @BeforeEach
    public void setUp() {
        when(apiTokenConfiguration.getDdTuningApiToken()).thenReturn("test");
    }

    @Test
    void getAllProductsFromAPI() {
        DDResponse response = prepareResponse();

        when(ddTuningFeignClient.getAllProducts(anyInt(), eq("test"))).thenReturn(response);

        List<Product> allProductsFromAPI = dataProvider.getAllProductsFromAPI();
        assertEquals(6, allProductsFromAPI.size());
    }

    @Test
    void getAllProductsFromApiFeignClientThrowsException() {
        when(ddTuningFeignClient.getAllProducts(anyInt(), eq("test"))).thenThrow(FeignException.NotFound.class);

        List<Product> allProductsFromAPI = dataProvider.getAllProductsFromAPI();
        assertEquals(0, allProductsFromAPI.size());
    }

    private DDResponse prepareResponse() {
        DDResponse response = new DDResponse();
        Product product = new Product();
        product.setId(1);
        product.setCategory("test");
        product.setTitle("test");
        response.data(List.of(product));
        return response;
    }
}