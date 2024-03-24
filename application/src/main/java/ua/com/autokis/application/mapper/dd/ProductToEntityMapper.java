package ua.com.autokis.application.mapper.dd;

import ua.com.autokis.application.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductToEntityMapper {
    Optional<ProductEntity> mapDtoToEntity(Object productDto);
    List<Optional<ProductEntity>> mapAllDtoToEntity(List< ? extends Object> productDtos);
}
