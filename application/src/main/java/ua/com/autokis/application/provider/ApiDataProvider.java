package ua.com.autokis.application.provider;

import ua.com.autokis.domain.product.dd.DDProduct;

import java.util.List;

public interface ApiDataProvider {
    List<DDProduct> getAllProductsFromAPI();
}
