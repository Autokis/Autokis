package ua.com.autokis.application.service.review;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.com.autokis.application.entity.ProductEntity;
import ua.com.autokis.application.entity.review.ReviewEntity;
import ua.com.autokis.application.repository.ProductRepository;
import ua.com.autokis.application.repository.review.ReviewRepository;
import ua.com.autokis.openapi.api.ReviewApiDelegate;
import ua.com.autokis.openapi.model.ReviewDTO;
import ua.com.autokis.openapi.model.ReviewRequestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewApiDelegate {
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    @Override
    public ResponseEntity<ReviewDTO> createNewReview(ReviewRequestDto reviewRequestDto) {
        ReviewEntity parsedEntity = modelMapper.map(reviewRequestDto, ReviewEntity.class);
        ProductEntity productEntity = productRepository.findById(reviewRequestDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Can`t find product with id: %d".formatted(reviewRequestDto.getProductId())));
        parsedEntity.setProduct(productEntity);

        ReviewEntity savedEntity = reviewRepository.save(parsedEntity);
        ReviewDTO reviewDTO = modelMapper.map(savedEntity, ReviewDTO.class);
        reviewDTO.setCreationDate(savedEntity.getCreationDate().toString());
        reviewDTO.setProductId(savedEntity.getProduct().getProductId());
        return ResponseEntity.ok(reviewDTO);
    }

    @Override
    public ResponseEntity<List<ReviewDTO>> getNReviews(Integer quantity) {
        Sort sort = Sort.by("creationDate").descending();
        Page<ReviewEntity> sorted = reviewRepository.findAll(PageRequest.of(0, quantity, sort));
        List<ReviewDTO> reviewDTOS = mapReviewEntityToReviewDto(sorted.stream().toList());
        return ResponseEntity.ok(reviewDTOS);
    }

    @Override
    public ResponseEntity<List<ReviewDTO>> getReviewsByProductId(Integer productId) {
        Optional<ProductEntity> productEntity = productRepository.findById(productId);

        if (productEntity.isEmpty()) {
            return ResponseEntity.ok(new ArrayList<>());
        }

        List<ReviewEntity> reviews = reviewRepository.findByProduct(productEntity.get());
        return ResponseEntity.ok(mapReviewEntityToReviewDto(reviews));
    }

    private List<ReviewDTO> mapReviewEntityToReviewDto(List<ReviewEntity> reviewEntities) {
        return reviewEntities.stream().map(e -> {
            ReviewDTO dto = modelMapper.map(e, ReviewDTO.class);
            dto.setCreationDate(e.getCreationDate().toString());
            dto.setProductId(e.getProduct().getProductId());
            return dto;
        }).toList();
    }
}
