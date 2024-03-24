package ua.com.autokis.application.mapper.dd.impl;

import org.springframework.stereotype.Component;
import ua.com.autokis.application.entity.product.ProductEntity;
import ua.com.autokis.application.mapper.dd.ProductToEntityMapper;
import ua.com.autokis.domain.product.dd.DDProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DDProductToEntityMapper implements ProductToEntityMapper {
    @Override
    public Optional<ProductEntity> mapDtoToEntity(Object productDto) {
        if (productDto instanceof DDProduct ddProduct) {
            List<String> additionalImages = new ArrayList<>();
            String image = null;
            if (ddProduct.getImages() != null) {
                additionalImages = ddProduct.getImages().size() < 2
                        ? new ArrayList<>()
                        : ddProduct.getImages().subList(1, ddProduct.getImages().size());
                image = !ddProduct.getImages().isEmpty()
                        ? ddProduct.getImages().get(0)
                        : null;
            }
            String productCategory = ddProduct.getSubcategory().isEmpty()
                    ? ddProduct.getCategory()
                    : ddProduct.getCategory() + ">" + ddProduct.getSubcategory();
            ProductEntity productEntity = ProductEntity.builder()
                    .productId(ddProduct.getId())
                    .model(ddProduct.getModel())
                    .manufacturer(ddProduct.getManufacturer())
                    .name(ddProduct.getTitle())
                    .image(image)
                    .additionalImages(additionalImages)
                    .price(ddProduct.getPrice())
                    .quantity(ddProduct.getQuantity())
                    .stockStatus(ddProduct.getQuantity() > 0
                            ? ProductStatus.AVAILABLE
                            : ProductStatus.OUT_OF_STOCK)
                    .sku(ddProduct.getSku())
                    .productCategory(productCategory)
                    .build();
            return Optional.of(productEntity);
        }
        return Optional.empty();
    }

    @Override
    public List<Optional<ProductEntity>> mapAllDtoToEntity(List<? extends Object> productDtos) {
        return productDtos.stream().map(this::mapDtoToEntity).toList();
    }
}
