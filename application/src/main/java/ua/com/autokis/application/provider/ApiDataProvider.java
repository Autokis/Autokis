package ua.com.autokis.application.provider;

import ua.com.autokis.openapi.model.Product;
import java.util.List;

public interface ApiDataProvider {
    List<Product> getAllProductsFromAPI();
}
