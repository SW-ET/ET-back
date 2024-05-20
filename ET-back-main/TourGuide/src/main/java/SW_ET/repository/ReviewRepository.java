package SW_ET.repository;

import SW_ET.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTagsContaining(String tag);  // Find reviews by tags
}
