package SW_ET.repository;

import SW_ET.entity.ReviewImages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ReviewImages, Long> {
    // Methods to find images by reviewId, etc., can be added if necessary
}