package ua.com.autokis.application.entity.review;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import ua.com.autokis.application.entity.ProductEntity;

import java.time.Instant;

@Data
@Builder
@Entity
@Table(name = "review")
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private Integer userId;
    private int review;
    private String comment;
    @CreationTimestamp
    private Instant creationDate;
}
