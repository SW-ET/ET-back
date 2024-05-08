package SW_ET.service;

import SW_ET.dto.UserDto;
import SW_ET.entity.User;
import SW_ET.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void registerUser(UserDto userDto) {
        User newUser = new User();
        newUser.setUserName(userDto.getUserName());
        newUser.setUserEmail(userDto.getUserEmail());
        newUser.setUserPassword(passwordEncoder.encode(userDto.getUserPassword()));
        newUser.setRegistDate(LocalDate.now());  // 날짜는 여기에서 설정
        userRepository.save(newUser);
    }
}
