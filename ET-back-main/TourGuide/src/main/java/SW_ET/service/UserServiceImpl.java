package SW_ET.service;

import SW_ET.dto.UserDto;
import SW_ET.entity.User;
import SW_ET.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

// @Service 애너테이션은 이 클래스가 스프링 컨텍스트에서 서비스 레이어의 컴포넌트로 관리되어야 함을 나타냅니다.
@Service
// @RequiredArgsConstructor는 final 또는 @NonNull 필드에 대한 생성자를 자동으로 생성합니다.
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    // userRepository와 passwordEncoder는 생성자 주입을 통해 자동으로 주입됩니다.
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // @Transactional은 이 메소드가 데이터베이스 트랜잭션의 일부로 실행되어야 함을 나타냅니다.
    @Transactional
    public void registerUser(UserDto userDto) {
        // User 엔티티의 새 인스턴스를 생성합니다.
        User user = new User();
        // UserDto에서 받은 데이터를 User 엔티티에 설정합니다.
        user.setUserName(userDto.getUserName());  // 사용자 이름 설정
        user.setUserEmail(userDto.getUserEmail());  // 사용자 이메일 설정
        // 사용자 비밀번호를 인코딩하여 설정합니다.
        user.setUserPassword(passwordEncoder.encode(userDto.getUserPassword()));
        // 현재 날짜를 가입 날짜로 설정합니다.
        user.setRegistDate(LocalDate.now());
        // 구성된 User 객체를 데이터베이스에 저장합니다.
        userRepository.save(user);
    }
}
