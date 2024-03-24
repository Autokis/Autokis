package ua.com.autokis.application.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.com.autokis.application.entity.ProductEntity;
import ua.com.autokis.application.mapper.dd.ProductToEntityMapper;
import ua.com.autokis.application.provider.ApiDataProvider;
import ua.com.autokis.application.repository.ProductRepository;
import ua.com.autokis.openapi.api.ProductsApiDelegate;
import ua.com.autokis.openapi.model.ProductDTO;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductsApiDelegate {
    private final ApiDataProvider dataProvider;
    private final ProductToEntityMapper productToEntityMapper;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    @Override
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductEntity> productEntities = productRepository.findAll();

        return ResponseEntity.ok().body(productEntities.stream()
                .map(p -> modelMapper.map(p, ProductDTO.class))
                .toList());
    }

    @Override
    public ResponseEntity<List<ProductDTO>> getNNewProducts(Integer quantity) {
        Sort sort = Sort.by("dateAdded").descending();
        Page<ProductEntity> productEntities = productRepository.findAll(PageRequest.of(0, quantity, sort));
        List<ProductDTO> productDTOS = productEntities.get().map(e -> modelMapper.map(e, ProductDTO.class)).toList();
        return ResponseEntity.ok(productDTOS);
    }

    @Override
    public ResponseEntity<Void> productsImportGet() {
        importAllProductsIntoDb();
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ProductDTO> editProduct(ProductDTO productDTO) {
        ProductEntity productEntity = productRepository.save(modelMapper.map(productDTO, ProductEntity.class));
        return ResponseEntity.ok(modelMapper.map(productEntity, ProductDTO.class));
    }

    @Override
    public ResponseEntity<ProductDTO> createNewProduct(ProductDTO productDTO) {
        ProductEntity productEntity = productRepository.save(modelMapper.map(productDTO, ProductEntity.class));
        return ResponseEntity.ok(modelMapper.map(productEntity, ProductDTO.class));
    }

    @Override
    public ResponseEntity<Void> deleteProductById(Integer productId) {
        productRepository.deleteById(productId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ProductDTO> getProductById(Integer id) {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Product with id %s not found".formatted(id)));
        return ResponseEntity.ok(modelMapper.map(productEntity, ProductDTO.class));
    }

    @Scheduled(cron = "${job.data.import.cron}")
    public void importAllProductsIntoDb() {
        List<ProductEntity> productEntities = productToEntityMapper
                .mapAllDtoToEntity(dataProvider.getAllProductsFromAPI())
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        productRepository.saveAll(productEntities);
    }
}
