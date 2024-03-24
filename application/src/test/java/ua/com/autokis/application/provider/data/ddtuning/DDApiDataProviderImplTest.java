package ua.com.autokis.application.provider.data.ddtuning;

import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;
import ua.com.autokis.api.client.feign.DDTuningFeignClient;
import ua.com.autokis.application.config.ApiTokenConfiguration;
import ua.com.autokis.domain.product.dd.DDProduct;
import ua.com.autokis.domain.response.DDResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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

        List<DDProduct> allProductsFromAPI = dataProvider.getAllProductsFromAPI();
        assertEquals(response.getTotalResults() / PRODUCTS_PER_REQUEST, allProductsFromAPI.size());
    }

    @Test
    void getAllProductsFromApiFeignClientThrowsException() {
        DDResponse response = prepareResponse();
        when(ddTuningFeignClient.getAllProducts(0, "test", 1)).thenReturn(response);
        when(ddTuningFeignClient.getAllProducts(anyInt(), eq("test"), eq(PRODUCTS_PER_REQUEST)))
                .thenThrow(FeignException.NotFound.class);

        List<DDProduct> allProductsFromAPI = dataProvider.getAllProductsFromAPI();
        assertEquals(0, allProductsFromAPI.size());
    }

    private DDResponse prepareResponse() {
        int totalResults = 60_000;
        DDResponse response = new DDResponse();
        DDProduct product = new DDProduct();

        product.setId(1);
        product.setCategory("test");
        product.setTitle("test");
        response.setTotalResults(totalResults);
        response.setData(List.of(product));
        return response;
    }
}