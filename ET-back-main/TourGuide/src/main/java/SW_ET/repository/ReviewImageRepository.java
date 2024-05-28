package SW_ET.repository;

import SW_ET.entity.ReviewImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImages, Long> {
    // 필요한 추가 메소드가 있다면 여기에 정의할 수 있습니다.
}