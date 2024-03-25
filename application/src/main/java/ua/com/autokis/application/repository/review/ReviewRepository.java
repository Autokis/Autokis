package ua.com.autokis.application.repository.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.autokis.application.entity.ProductEntity;
import ua.com.autokis.application.entity.review.ReviewEntity;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {
    List<ReviewEntity> findByProduct(ProductEntity product);
}
