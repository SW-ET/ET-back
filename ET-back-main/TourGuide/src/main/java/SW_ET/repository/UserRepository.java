package SW_ET.repository;

import SW_ET.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {  // ID의 타입을 String으로 변경
}
