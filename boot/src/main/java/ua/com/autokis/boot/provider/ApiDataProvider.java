package ua.com.autokis.boot.provider;

import ua.com.autokis.openapi.model.Product;
import java.util.List;

public interface ApiDataProvider {
    List<Product> getAllProductsFromAPI();
}
