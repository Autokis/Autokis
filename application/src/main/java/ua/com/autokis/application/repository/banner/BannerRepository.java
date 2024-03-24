package ua.com.autokis.application.repository.banner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.autokis.application.entity.banner.BannerEntity;

@Repository
public interface BannerRepository extends JpaRepository<BannerEntity, Integer> {
}
