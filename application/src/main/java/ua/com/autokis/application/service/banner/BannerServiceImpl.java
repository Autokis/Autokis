package ua.com.autokis.application.service.banner;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.com.autokis.application.entity.banner.BannerEntity;
import ua.com.autokis.application.repository.banner.BannerRepository;
import ua.com.autokis.openapi.api.BannerApiDelegate;
import ua.com.autokis.openapi.model.BannerDTO;
import ua.com.autokis.openapi.model.BannerRequestDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerApiDelegate {
    private final BannerRepository bannerRepository;
    private final ModelMapper objectMapper;
    @Override
    public ResponseEntity<BannerDTO> createNewBanner(BannerRequestDTO bannerDTO) {
        BannerEntity bannerEntity = objectMapper.map(bannerDTO, BannerEntity.class);
        BannerEntity savedEntity = bannerRepository.save(bannerEntity);
        return ResponseEntity.ok(objectMapper.map(savedEntity, BannerDTO.class));
    }

    @Override
    public ResponseEntity<Void> deleteBannerById(Integer bannerId) {
        bannerRepository.deleteById(bannerId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<BannerDTO>> getAllBanners() {
        List<BannerDTO> bannerDTOS = bannerRepository.findAll().stream()
                .map(e -> objectMapper.map(e, BannerDTO.class)).toList();
        return ResponseEntity.ok(bannerDTOS);
    }
}
