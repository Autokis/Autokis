package ua.com.autokis.application.mapper.dd.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.autokis.application.entity.ProductEntity;
import ua.com.autokis.domain.product.dd.DDProduct;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DDProductToEntityMapperTest {
    private DDProductToEntityMapper mapper;

    @BeforeEach
    void init() {
        mapper = new DDProductToEntityMapper();
    }

    @Test
    void mapDtoToEntity() {
        DDProduct ddProduct = prepareDDProduct();

        Optional<ProductEntity> productEntity = mapper.mapDtoToEntity(ddProduct);

        assertTrue(productEntity.isPresent());
        compareEntityToDDProduct(ddProduct, productEntity.get());
    }

    @Test
    void testImagesAreNull() {
        DDProduct ddProduct = prepareDDProduct();
        ddProduct.setImages(null);

        Optional<ProductEntity> productEntity = mapper.mapDtoToEntity(ddProduct);

        assertTrue(productEntity.isPresent());
        assertNull(productEntity.get().getImage());
        assertTrue(productEntity.get().getAdditionalImages().isEmpty());
    }

    @Test
    void testPuttingRandomObjectInMethod() {
        ProductEntity product = new ProductEntity();

        Optional<ProductEntity> productEntity = mapper.mapDtoToEntity(product);

        assertTrue(productEntity.isEmpty());
    }

    @Test
    void mapAllDtoToEntity() {
        List<DDProduct> ddProducts = List.of(prepareDDProduct(), prepareDDProduct());

        List<Optional<ProductEntity>> optionals = mapper.mapAllDtoToEntity(ddProducts);

        optionals.forEach(e -> assertTrue(e.isPresent()));
        for (int i = 0; i < ddProducts.size(); i++) {
            compareEntityToDDProduct(ddProducts.get(i), optionals.get(i).get());
        }
    }

    private DDProduct prepareDDProduct() {
        DDProduct ddProduct = new DDProduct();
        ddProduct.setId(1);
        ddProduct.setModel("Test model");
        ddProduct.setManufacturer("Test manufacturer");
        ddProduct.setTitle("Test title");
        ddProduct.setImages(List.of("image1", "image2", "image3"));
        ddProduct.setPrice(10);
        ddProduct.setQuantity(2);
        ddProduct.setSku("Test SKU");
        ddProduct.setCategory("Test Category");
        ddProduct.setSubcategory("Test Subcategory");

        return ddProduct;
    }

    private void compareEntityToDDProduct(DDProduct ddProduct, ProductEntity productEntity) {
        String productCategory = ddProduct.getCategory() + ">" + ddProduct.getSubcategory();
        assertEquals(ddProduct.getId(), productEntity.getProductId());
        assertEquals(ddProduct.getModel(), productEntity.getModel());
        assertEquals(ddProduct.getManufacturer(),productEntity.getManufacturer());
        assertEquals(ddProduct.getTitle(), productEntity.getName());
        assertEquals(ddProduct.getImages().get(0), productEntity.getImage());
        assertEquals(ddProduct.getPrice(), productEntity.getPrice());
        assertEquals(ProductStatus.AVAILABLE, productEntity.getStockStatus());
        assertEquals(ddProduct.getQuantity(), productEntity.getQuantity());
        assertEquals(ddProduct.getSku(), productEntity.getSku());
        assertEquals(productCategory, productEntity.getProductCategory());
    }
}